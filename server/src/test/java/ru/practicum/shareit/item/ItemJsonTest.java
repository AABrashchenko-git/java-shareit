package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.model.ItemDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@JsonTest
class ItemJsonTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void testDeserializeItemDto() throws IOException {
        String jsonContent = "{\"id\": 1, \"name\": \"Item 1\"," +
                " \"description\": \"Description 1\", \"available\": true, \"requestId\": 2}";

        assertThatCode(() -> json.parse(jsonContent).getObject())
                .doesNotThrowAnyException();

        ItemDto result = json.parse(jsonContent).getObject();

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("Item 1");
        assertThat(result.getDescription()).isEqualTo("Description 1");
        assertThat(result.getAvailable()).isEqualTo(true);
        assertThat(result.getRequestId()).isEqualTo(2);
        assertThat(result.getOwnerId()).isNull();
    }
}