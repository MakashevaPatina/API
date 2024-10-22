CREATE TABLE Person (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    age INT CHECK (age >= 18),
    license BOOLEAN NOT NULL
);

CREATE TABLE Car (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(20) NOT NULL,
    model VARCHAR(20) NOT NULL,
    price NUMERIC(10, 2) CHECK (price >= 0)
);


CREATE TABLE Person_car (
    person_id INTEGER REFERENCES person(person_id),
    car_id INTEGER REFERENCES cars(car_id),
    PRIMARY KEY (person_id, car_id)
);


