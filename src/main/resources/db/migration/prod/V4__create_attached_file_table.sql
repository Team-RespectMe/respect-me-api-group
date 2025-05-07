
create table if not exists attachment (
    id uuid not null primary key,
    group_id uuid not null,
    notification_id uuid not null,
    type smallint not null,
    resource_id uuid not null
);