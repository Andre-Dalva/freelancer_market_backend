create table users
(
    id            bigint auto_increment
        primary key,
    name          varchar(255) not null,
    email         varchar(200) not null,
    password_hash varchar(255) not null,
    role          varchar(20)  not null,
    location      varchar(255) null,
    phone_number  varchar(255) null,
    created_at    timestamp    not null
);

create table freelancer_profile
(
    id            bigint
        primary key,
    skills        varchar(255) not null,
    languages     varchar(255) not null,
    date_of_birth date         not null,
    constraint freelancer_profile_users_id_fk
        foreign key (id) references users (id)
);

create table jobs
(
    id                    bigint auto_increment
        primary key,
    title                 varchar(255) not null,
    requirements          text         not null,
    deadlines             text         null,
    about                 text         not null,
    benefits_and_payments text         null,
    created_at            timestamp    not null,
    recruiter_id          bigint       not null,
    constraint jobs_users_id_fk
        foreign key (recruiter_id) references users (id)
);

create table applications
(
    id            bigint auto_increment
        primary key,
    job_id        bigint      not null,
    freelancer_id bigint      not null,
    created_at    timestamp   not null,
    status        varchar(30) not null,
    constraint applications_jobs_id_fk
        foreign key (job_id) references jobs (id),
    constraint applications_users_id_fk
        foreign key (freelancer_id) references users (id)
);

create table accepted_jobs
(
    application_id bigint
        primary key,
    status         varchar(80) not null,
    optional_link1 text        null,
    optional_link2 text        null,
    comments       text        null,
    assigned_at    timestamp   not null,
    finished_at    timestamp   null,
    constraint accepted_jobs_applications_id_fk
        foreign key (application_id) references applications (id)
);

