CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE TABLE room (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(20) NOT NULL UNIQUE,
                       location VARCHAR(20) NOT NULL,
                       capacity INT NOT NULL
);

CREATE TABLE reservation (
                              id BIGSERIAL PRIMARY KEY,
                              room_id BIGINT NOT NULL,
                              user_id BIGINT NOT NULL,
                              start_at TIMESTAMPTZ NOT NULL,
                              end_at TIMESTAMPTZ NOT NULL
);

ALTER TABLE reservation
    ADD CONSTRAINT reservation_no_overlap
    EXCLUDE USING gist (
    room_id WITH =,
    tstzrange(start_at, end_at) WITH &&
);
