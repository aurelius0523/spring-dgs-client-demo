package com.aurelius.springdgsclientdemo;

import com.aurelius.connectors.dgs.graphiqlonline.generated.client.*;
import com.aurelius.connectors.dgs.graphiqlonline.generated.types.*;
import com.aurelius.springdgsclientdemo.connectors.dgs.graphiqlonline.GraphqlZeroConnector;
import com.aurelius.springdgsclientdemo.models.PhotoAlbumAliasHolder;
import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.client.GraphQLResponse;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpringDgsClientDemoApplicationTests {

    @Autowired
    private GraphqlZeroConnector graphqlZeroConnector;

    @Test
    public void testBasicQuery() {
        GraphQLQueryRequest request = new GraphQLQueryRequest(
                new AlbumsGraphQLQuery.Builder()
                        .build(),
                new AlbumsProjectionRoot()
                        .data()
                        .title()
        );

        GraphQLResponse graphQLResponse = graphqlZeroConnector.executeGraphQLQuery(request);
        List<Album> albumList = graphQLResponse.extractValueAsObject("albums.data[*]", new TypeRef<>() {
        });

        assertFalse(albumList.isEmpty());
        assertEquals(100, albumList.size());
        assertEquals("quidem molestiae enim", albumList.get(0).getTitle());
    }

    @Test
    public void testBasicQueryWithNestedProjections() {
        GraphQLQueryRequest request = new GraphQLQueryRequest(
                new AlbumsGraphQLQuery.Builder()
                        .build(),
                new AlbumsProjectionRoot()
                        .data()
                        .title()
                        .user().id().name().username()
        );

        GraphQLResponse graphQLResponse = graphqlZeroConnector.executeGraphQLQuery(request);
        List<Album> albumList = graphQLResponse.extractValueAsObject("albums.data[*]", new TypeRef<>() {
        });

        assertFalse(albumList.isEmpty());
        assertEquals(100, albumList.size());
        final Album firstAlbum = albumList.get(0);
        assertEquals("quidem molestiae enim", firstAlbum.getTitle());
        assertEquals("1", firstAlbum.getUser().getId());
        assertEquals("Leanne Graham", firstAlbum.getUser().getName());
        assertEquals("Bret", firstAlbum.getUser().getUsername());
    }

    @Test
    public void testBasicQueryWithArgument() {
        GraphQLQueryRequest request = new GraphQLQueryRequest(
                new AlbumsGraphQLQuery.Builder()
                        .options(PageQueryOptions.newBuilder()
                                .slice(SliceOptions.newBuilder()
                                        .start(2)
                                        .build())
                                .build())
                        .build(),
                new AlbumsProjectionRoot()
                        .data()
                        .id()
                        .title()
                        .user().id().name().username()
        );

        GraphQLResponse graphQLResponse = graphqlZeroConnector.executeGraphQLQuery(request);
        List<Album> albumList = graphQLResponse.extractValueAsObject("albums.data[*]", new TypeRef<>() {
        });

        assertFalse(albumList.isEmpty());
        final Album firstAlbum = albumList.get(0);
        assertEquals("3", firstAlbum.getId());
        assertEquals("omnis laborum odio", firstAlbum.getTitle());
        assertEquals("1", firstAlbum.getUser().getId());
        assertEquals("Leanne Graham", firstAlbum.getUser().getName());
        assertEquals("Bret", firstAlbum.getUser().getUsername());
    }


    /**
     * dgs doesn't seem to support nested arguments so string query needs to be used instead
     */
    @Test
    public void testBasicQueryWithNestedArgument() {
        final String query = "query { albums (options: { slice:{limit:1}}) { data { id title photos (options:{search: {q: \"ex\"}}) { data { id title url } } } } }";

        GraphQLResponse graphQLResponse = graphqlZeroConnector.executeGraphQLQuery(query);
        List<Album> albumList = graphQLResponse.extractValueAsObject("albums.data[*]", new TypeRef<>() {
        });

        assertFalse(albumList.isEmpty());
        final Album firstAlbum = albumList.get(0);
        assertEquals(1, albumList.size());
        assertEquals("2", firstAlbum.getId());
        assertEquals("sunt qui excepturi placeat culpa", firstAlbum.getTitle());

        final List<Photo> photoList = firstAlbum.getPhotos().getData();
        assertEquals(5, photoList.size());

        final Photo firstPhoto = photoList.get(0);
        assertEquals("54", firstPhoto.getId());
        assertEquals("ut ex quibusdam dolore mollitia", firstPhoto.getTitle());
        assertEquals("https://via.placeholder.com/600/aa8f2e", firstPhoto.getUrl());
    }

    /**
     * Alias models are not generated by dgs client. Manual models need to be created to support the custom mapping
     */
    @Test
    public void testAlias() {
        final String query = "query { firstAlbum: albums(options: { search: { q: \"quidem molestiae\" } }) { data { id title } } secondAlbum: albums(options: { search: { q: \"sunt qui\" } }) { data { id title } } }";

        GraphQLResponse graphQLResponse = graphqlZeroConnector.executeGraphQLQuery(query);
        PhotoAlbumAliasHolder photoAlbumAliasHolder = graphQLResponse.extractValueAsObject("data", new TypeRef<>() {
        });

        assertNotNull(photoAlbumAliasHolder);
        assertNotNull(photoAlbumAliasHolder.getFirstAlbum());
        assertNotNull(photoAlbumAliasHolder.getSecondAlbum());

        final Album firstAlbum = photoAlbumAliasHolder.getFirstAlbum().getData().get(0);
        assertEquals("1", firstAlbum.getId());
        assertEquals("quidem molestiae enim", firstAlbum.getTitle());

        final Album secondAlbum = photoAlbumAliasHolder.getSecondAlbum().getData().get(0);
        assertEquals("2", secondAlbum.getId());
        assertEquals("sunt qui excepturi placeat culpa", secondAlbum.getTitle());
    }


    @Test
    public void testMutation() {
        final String NEW_BODY_STRING = "New Body";
        final String NEW_TITLE_STRING = "New Title";

        GraphQLQueryRequest request = new GraphQLQueryRequest(
                new CreatePostGraphQLQuery.Builder()
                        .input(CreatePostInput.newBuilder()
                                .body(NEW_BODY_STRING)
                                .title(NEW_TITLE_STRING)
                                .build())
                        .build(),
                new CreatePostProjectionRoot()
                        .id().body().title()
        );

        GraphQLResponse graphQLResponse = graphqlZeroConnector.executeGraphQLQuery(request);
        Post post = graphQLResponse.extractValueAsObject("data.createPost", new TypeRef<>() {
        });

        assertNotNull(post);

        assertEquals("101", post.getId());
        assertEquals(NEW_BODY_STRING, post.getBody());
        assertEquals(NEW_TITLE_STRING, post.getTitle());
    }
}
