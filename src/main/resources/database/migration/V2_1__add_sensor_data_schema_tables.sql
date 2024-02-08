CREATE TABLE IF NOT EXISTS sensor_data.classroom_energy_consumption (
    classroom_id varchar(3) NOT NULL,
    total_consumption INT DEFAULT 0 NOT NULL,
    date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT classroom_energy_consumption_pkey PRIMARY KEY (classroom_id, date_time),
    CONSTRAINT classroom_id_fkey FOREIGN KEY (classroom_id) REFERENCES ufpel_data.classrooms(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

--ALTER TABLE IF EXISTS ufpel_data.user_in_course
    --OWNER to admin;