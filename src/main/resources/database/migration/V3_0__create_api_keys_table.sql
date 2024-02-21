CREATE TABLE IF NOT EXISTS sensor_data.api_keys
(
    active boolean NOT NULL,
    expiration date,
    last_used timestamp(6) without time zone,
    key uuid NOT NULL,
    CONSTRAINT api_keys_pkey PRIMARY KEY (key)
);
