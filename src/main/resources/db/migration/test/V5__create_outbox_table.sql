create table if not exists message_outbox (
    message_id uuid not null primary key,
    event_name varchar(255) not null,
    processing_id uuid default null,
    status smallint default 0,
    created_at timestamp(6) without time zone default current_timestamp,
    updated_at timestamp(6) without time zone default null,
    message jsonb
);