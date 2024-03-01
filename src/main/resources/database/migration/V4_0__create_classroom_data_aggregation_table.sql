CREATE TABLE IF NOT EXISTS sensor_data.classroom_data_aggregation (
    classroom_id varchar(3) NOT NULL,
    avg_consumption INT,
    min_consumption INT,
    max_consumption INT,
    std_consumption INT,
    aggregation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT classroom_data_aggregation_pkey PRIMARY KEY (classroom_id, aggregation_date),
    CONSTRAINT aggregation_classroom_id_fkey FOREIGN KEY (classroom_id) REFERENCES ufpel_data.classrooms(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
    );