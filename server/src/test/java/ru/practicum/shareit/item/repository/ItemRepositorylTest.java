package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.practicum.server.item.dto.CommentDto;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.item.dto.ItemDtoWithComments;
import ru.practicum.server.item.mapper.CommentMapper;
import ru.practicum.server.item.model.Comment;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.service.ItemService;
import ru.practicum.server.item.storage.CommentRepository;
import ru.practicum.server.item.storage.ItemRepository;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRepositorylTest {
    private final JdbcTemplate jdbcTemplate;
    private final UserService userService;
    private final ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CommentRepository commentRepository;

    private UserDto userDto1 = new UserDto(
            null,
            "User1",
            "some1@email.com");
    private UserDto userDto2 = new UserDto(
            null,
            "User2",
            "some2@email.com");


    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM items ");
        jdbcTemplate.update("DELETE FROM users ");
        jdbcTemplate.update("ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE ITEMS ALTER COLUMN ID RESTART WITH 1");
        userService.createUser(userDto1);
        userService.createUser(userDto2);
    }

    @Test
    void getItemByIdTest() {
        ItemDto itemDto1 = new ItemDto(null, "Item1", "Item1 description", true,
                null, null, null);
        ItemDto itemDto2 = new ItemDto(null, "Item2", "Item2 description", true,
                null, null, null);

        itemService.createItem(1L, itemDto1);
        itemService.createItem(1L, itemDto2);

        ItemDtoWithComments item = itemService.getItemById(1L, 1L);
        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(), equalTo(itemDto1.getName()));
        assertThat(item.getDescription(), equalTo(itemDto1.getDescription()));
        assertThat(item.getAvailable(), equalTo(itemDto1.getAvailable()));
        assertThat(item.getLastBooking(), nullValue());
        assertThat(item.getNextBooking(), nullValue());
        assertThat(item.getComments().size(), equalTo(0));
    }

    @Test
    void save() {
        Item item = new Item(
                null,
                "Item1",
                "Item1 description",
                true,
                1L,
                null);
        Item gotItem = itemRepository.save(item);
        assertThat(gotItem.getId(), notNullValue());
        assertThat(gotItem.getName(), equalTo(item.getName()));
        assertThat(gotItem.getDescription(), equalTo(item.getDescription()));
        assertThat(gotItem.getAvailable(), equalTo(item.getAvailable()));
        assertThat(gotItem.getOwner(), equalTo(item.getOwner()));
        assertThat(gotItem.getRequestId(), equalTo(item.getRequestId()));
    }

    @Test
    void deleteItem() {
        Item item = new Item(
                null,
                "Item1",
                "Item1 description",
                true,
                1L,
                null);
        Item gotItem = itemRepository.save(item);

        assertThat(itemRepository.existsById(1L), equalTo(true));
        itemRepository.deleteById(1L);
        assertThat(itemRepository.existsById(1L), equalTo(false));

    }

    @Test
    void getItemsByOwnerOrderById() {
        Item item = new Item(
                null,
                "Item1",
                "Item1 description",
                true,
                1L,
                null);
        Item item2 = new Item(
                null,
                "Item2",
                "Item2 description",
                true,
                1L,
                null);
        itemRepository.save(item);
        itemRepository.save(item2);
        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());
        Collection<Item> items = itemRepository.getItemsByOwnerOrderById(1L, pageRequest);
        Item gotItem = items.stream().findFirst().get();
        assertThat(gotItem.getId(), notNullValue());
        assertThat(gotItem.getName(), equalTo(item.getName()));
        assertThat(gotItem.getDescription(), equalTo(item.getDescription()));
        assertThat(gotItem.getAvailable(), equalTo(item.getAvailable()));
        assertThat(gotItem.getOwner(), equalTo(item.getOwner()));
        assertThat(gotItem.getRequestId(), equalTo(item.getRequestId()));

    }

    @Test
    void getItemsByOwnerOrderById2() {
        Item item = new Item(
                null,
                "Item1",
                "Item1 description",
                true,
                1L,
                null);
        Item item2 = new Item(
                null,
                "Item2",
                "Item2 description",
                true,
                1L,
                null);
        itemRepository.save(item);
        itemRepository.save(item2);
        PageRequest pageRequest = PageRequest.of(1, 1, Sort.unsorted());
        Collection<Item> items = itemRepository.getItemsByOwnerOrderById(1L, pageRequest);
        Item gotItem = items.stream().findFirst().get();
        assertThat(items.size(), equalTo(1));
        assertThat(gotItem.getId(), notNullValue());
        assertThat(gotItem.getName(), equalTo(item2.getName()));
        assertThat(gotItem.getDescription(), equalTo(item2.getDescription()));
        assertThat(gotItem.getAvailable(), equalTo(item2.getAvailable()));
        assertThat(gotItem.getOwner(), equalTo(item2.getOwner()));
        assertThat(gotItem.getRequestId(), equalTo(item2.getRequestId()));

    }

    @Test
    void searchItems() {
        Item item = new Item(
                null,
                "Item1",
                "Item1 description",
                true,
                1L,
                null);
        Item item2 = new Item(
                null,
                "Item2",
                "Гиря",
                true,
                1L,
                null);
        itemRepository.save(item);
        itemRepository.save(item2);

        Collection<Item> items = itemRepository.searchItems("Гиря");
        Item gotItem = items.stream().findFirst().get();
        assertThat(items.size(), equalTo(1));
        assertThat(gotItem.getId(), notNullValue());
        assertThat(gotItem.getName(), equalTo(item2.getName()));
        assertThat(gotItem.getDescription(), equalTo(item2.getDescription()));
        assertThat(gotItem.getAvailable(), equalTo(item2.getAvailable()));
        assertThat(gotItem.getOwner(), equalTo(item2.getOwner()));
        assertThat(gotItem.getRequestId(), equalTo(item2.getRequestId()));
    }

    @Test
    void existsById() {
        Item item = new Item(
                null,
                "Item1",
                "Item1 description",
                true,
                1L,
                null);
        itemRepository.save(item);

        assertThat(itemRepository.existsById(1L), equalTo(true));
    }

    @Test
    void createComment() {
        Item item = new Item(
                null,
                "Item1",
                "Item1 description",
                true,
                1L,
                null);
        Item item2 = new Item(
                null,
                "Item2",
                "Гиря",
                true,
                1L,
                null);
        itemRepository.save(item);
        itemRepository.save(item2);
        Comment comment1 = new Comment(
                null,
                "Text",
                1L,
                new User(1L, "Name", "mail@mail.ry"),
                LocalDateTime.now());

        Comment comment = commentRepository.save(comment1);
        assertThat(comment.getId(), notNullValue());
        assertThat(comment.getItem(), equalTo(comment1.getItem()));
        assertThat(comment.getText(), equalTo(comment1.getText()));
        assertThat(comment.getAuthor().getId(), equalTo(comment1.getAuthor().getId()));
        assertThat(comment.getCreated(), equalTo(comment1.getCreated()));
    }

    @Test
    void getCommentsByItemEquals() {
        Item item = new Item(
                null,
                "Item1",
                "Item1 description",
                true,
                1L,
                null);
        Item item2 = new Item(
                null,
                "Item2",
                "Гиря",
                true,
                1L,
                null);
        itemRepository.save(item);
        itemRepository.save(item2);
        Comment comment1 = new Comment(
                null,
                "Text",
                1L,
                new User(1L, "Name", "mail@mail.ry"),
                LocalDateTime.now());
        Comment comment2 = new Comment(
                null,
                "Text",
                2L,
                new User(1L, "Name", "mail@mail.ry"),
                LocalDateTime.now());
        commentRepository.save(comment1);
        commentRepository.save(comment2);

        Collection<Comment> comments = commentRepository.getCommentsByItemEquals(1L);
        Comment comment = comments.stream().findFirst().get();
        assertThat(comment.getId(), notNullValue());
        assertThat(comment.getItem(), equalTo(comment1.getItem()));
        assertThat(comment.getText(), equalTo(comment1.getText()));
        assertThat(comment.getAuthor().getId(), equalTo(comment1.getAuthor().getId()));
        assertThat(comment.getCreated(), equalTo(comment1.getCreated()));
    }

    @Test
    void commentMapperTest() {
        Comment comment = new Comment(
                null,
                null,
                null,
                null,
                null);

        CommentDto comment1 = CommentMapper.INSTANCE.toCommentDto(comment);

        assertThat(comment1.getId(), equalTo(null));
        assertThat(comment1.getItem(), equalTo(null));
        assertThat(comment1.getText(), equalTo(null));
        assertThat(comment1.getAuthorName(), equalTo(null));
        assertThat(comment1.getAuthorId(), equalTo(null));
    }

    @Test
    void commentMapperTest2() {
        CommentDto comment = new CommentDto(
                null,
                null,
                null,
                null,
                null);

        Comment comment1 = CommentMapper.INSTANCE.toComment(comment);

        assertThat(comment1.getId(), equalTo(null));
        assertThat(comment1.getItem(), equalTo(null));
        assertThat(comment1.getText(), equalTo(null));
        assertThat(comment1.getAuthor().getId(), equalTo(null));
        assertThat(comment1.getItem(), equalTo(null));
    }

    @Test
    void commentMapperTest3() {
        Comment comment = new Comment(
                null,
                null,
                null,
                null,
                null);
        Collection<Comment> comments = new ArrayList<>();
        comments.add(comment);
        Collection<CommentDto> gotComments = CommentMapper.INSTANCE.toCommentDtos(comments);
        CommentDto comment1 = gotComments.stream().findFirst().get();
        assertThat(comment1.getId(), equalTo(null));
        assertThat(comment1.getItem(), equalTo(null));
        assertThat(comment1.getText(), equalTo(null));
        assertThat(comment1.getAuthorId(), equalTo(null));
        assertThat(comment1.getItem(), equalTo(null));
    }
}
