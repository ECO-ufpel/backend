ALTER TABLE IF EXISTS sensor_data.classroom_energy_consumption
    RENAME total_consumption TO consumption;

ALTER TABLE sensor_data.classroom_energy_consumption
ALTER COLUMN consumption TYPE double precision;