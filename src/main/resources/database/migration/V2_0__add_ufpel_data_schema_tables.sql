CREATE TABLE IF NOT EXISTS ufpel_data.courses
(
    id varchar(8) UNIQUE NOT NULL,
    title varchar(256) NOT NULL,
    CONSTRAINT courses_pkey PRIMARY  KEY (id)
);

CREATE TABLE IF NOT EXISTS ufpel_data.user_in_course (
    user_cpf varchar(11) NOT NULL,
    course_id varchar(8) NOT NULL,
    CONSTRAINT user_in_course_pkey PRIMARY KEY (user_cpf, course_id),
    CONSTRAINT user_cpf_fkey FOREIGN KEY (user_cpf) REFERENCES users.users(cpf)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT course_id_fkey FOREIGN KEY (course_id) REFERENCES ufpel_data.courses(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

--ALTER TABLE IF EXISTS ufpel_data.user_in_course
    --OWNER to admin;

CREATE TABLE IF NOT EXISTS ufpel_data.classrooms (
    id varchar(3) NOT NULL,
    CONSTRAINT classrooms_pkey PRIMARY  KEY (id)
);

--ALTER TABLE IF EXISTS ufpel_data.classrooms
    --OWNER to admin;

CREATE TABLE IF NOT EXISTS ufpel_data.time_intervals (
    id SMALLSERIAL NOT NULL,
    start_time TIME WITHOUT TIME ZONE NOT NULL UNIQUE,
    end_time TIME WITHOUT TIME ZONE NOT NULL UNIQUE,
    CONSTRAINT time_intervals_pkey PRIMARY KEY (id)
);

--ALTER TABLE IF EXISTS ufpel_data.classrooms
    --OWNER to admin;

CREATE TABLE IF NOT EXISTS ufpel_data.course_in_room (
    course_id varchar(8) NOT NULL,
    classroom_id varchar(3) NOT NULL,
    interval SMALLINT NOT NULL,
    CONSTRAINT course_in_room_pkey PRIMARY KEY (classroom_id, interval),
    CONSTRAINT interval_fkey FOREIGN KEY (interval) REFERENCES ufpel_data.time_intervals(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

--ALTER TABLE IF EXISTS ufpel_data.classrooms
    --OWNER to admin;


