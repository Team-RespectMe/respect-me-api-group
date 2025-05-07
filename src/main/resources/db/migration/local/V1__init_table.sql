CREATE TABLE IF NOT EXISTS notification_group_member(
    id UUID,
    group_id UUID,
    member_id UUID,
    profile_image_url varchar(255),
    nickname varchar(64) not null,
    member_role smallint default 3,
    created_at timestamp(6) default current_timestamp,
    updated_at timestamp(6) default current_timestamp,
    is_deleted boolean default false,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS notification_group(
    id UUID,
    owner_id UUID,
    name varchar(64) not null,
    type smallint default 2,
    description varchar(255) default null,
    password varchar(255),
    created_at timestamp(6) default current_timestamp,
    updated_at timestamp(6) default current_timestamp,
    is_deleted boolean default false,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS group_notification(
    id UUID,
    group_id UUID,
    sender_id UUID,
    type smallint default 1,
    status smallint default 1,
    content text not null,
    created_at timestamp(6) default current_timestamp,
    updated_at timestamp(6) default current_timestamp,
    scheduled_at timestamp(6) default current_timestamp,
    day_of_weeks smallint default 0,
    day_interval smallint default 0,
    trigger_time time default '00:00:00',
    last_sent_at timestamp(6) default null,
    PRIMARY KEY (id)
);

-- 외래 키 설정 및 관계 정의

-- notification_group_member 테이블에서 group_id는 notification_group 테이블의 id를 참조
ALTER TABLE IF EXISTS notification_group_member
    ADD CONSTRAINT fk_notification_group_member_group_id
    FOREIGN KEY (group_id) REFERENCES notification_group(id) ON DELETE CASCADE;

-- group_notification 테이블에서 group_id는 notification_group 테이블의 id를 참조
ALTER TABLE IF EXISTS group_notification
    ADD CONSTRAINT fk_group_notification_group_id
    FOREIGN KEY (group_id) REFERENCES notification_group(id) ON DELETE CASCADE;