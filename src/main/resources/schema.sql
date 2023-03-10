CREATE TABLE IF NOT EXISTS users
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    name
    VARCHAR
(
    255
) NOT NULL,
    email VARCHAR
(
    512
) NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE
(
    email
),
    CONSTRAINT pk_user PRIMARY KEY
(
    id
)
    );

CREATE TABLE IF NOT EXISTS items
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    name
    VARCHAR
(
    255
) NOT NULL,
    description VARCHAR
(
    512
) NOT NULL,
    is_available BOOL,
    owner_id BIGINT,
    request_id BIGINT,
    CONSTRAINT pk_item PRIMARY KEY
(
    id
)
    );

CREATE TABLE IF NOT EXISTS bookings
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    start_date
    TIMESTAMP
    WITHOUT
    TIME
    ZONE,
    end_date
    TIMESTAMP
    WITHOUT
    TIME
    ZONE,
    item_id
    BIGINT,
    booker_id
    BIGINT,
    status
    VARCHAR
(
    125
) NOT NULL,
    CONSTRAINT pk_booker PRIMARY KEY
(
    id
)
    );

CREATE TABLE IF NOT EXISTS comments
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    text
    VARCHAR
(
    512
) NOT NULL,
    item_id BIGINT,
    author_id BIGINT,
    created TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_comments PRIMARY KEY
(
    id
)
    );

CREATE TABLE IF NOT EXISTS requests
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    description
    VARCHAR
(
    512
) NOT NULL,
    requestor_id BIGINT,
    created TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_request PRIMARY KEY
(
    id
)
    );

ALTER TABLE items
    ADD FOREIGN KEY (request_id) REFERENCES requests (id);
ALTER TABLE items
    ADD FOREIGN KEY (owner_id) REFERENCES users (id);
ALTER TABLE bookings
    ADD FOREIGN KEY (item_id) REFERENCES items (id);
ALTER TABLE bookings
    ADD FOREIGN KEY (booker_id) REFERENCES users (id);
ALTER TABLE comments
    ADD FOREIGN KEY (item_id) REFERENCES items (id);
ALTER TABLE comments
    ADD FOREIGN KEY (author_id) REFERENCES users (id);
ALTER TABLE requests
    ADD FOREIGN KEY (requestor_id) REFERENCES users (id);

DELETE
FROM comments;
DELETE
FROM bookings;
DELETE
FROM items;
DELETE
FROM requests;
DELETE
FROM users;

ALTER TABLE bookings
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE users
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE items
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE comments
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE requests
    ALTER COLUMN id RESTART WITH 1;