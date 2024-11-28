CREATE TABLE IF NOT EXISTS users (
        user_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
        email VARCHAR NOT NULL UNIQUE,
        name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS items (
        item_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
        name VARCHAR NOT NULL,
        description VARCHAR NOT NULL,
        available BOOLEAN NOT NULL,
        owner_id BIGINT NOT NULL,
        FOREIGN KEY (owner_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS bookings (
        booking_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
        start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
        end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
        item_id BIGINT NOT NULL,
        booker_id BIGINT NOT NULL,
        status VARCHAR NOT NULL,
        FOREIGN KEY (booker_id) REFERENCES users (user_id) ON DELETE CASCADE,
        FOREIGN KEY (item_id) REFERENCES items (item_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments (
        comment_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
        text VARCHAR NOT NULL,
        item_id BIGINT NOT NULL,
        author_id BIGINT NOT NULL,
        created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
        FOREIGN KEY (author_id) REFERENCES users (user_id) ON DELETE CASCADE,
        FOREIGN KEY (item_id) REFERENCES items (item_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS requests (
        request_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
        description VARCHAR NOT NULL,
        requestor_id BIGINT NOT NULL,
        created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
        FOREIGN KEY (requestor_id) REFERENCES users (user_id) ON DELETE CASCADE
);