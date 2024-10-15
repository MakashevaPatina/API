ALTER TABLE Student
ADD CONSTRAINT chk_age CHECK (age >= 16);
ALTER COLUMN age SET DEFAULT '20';
ALTER COLUMN name SET NOT NULL;
ADD CONSTRAINT student_name UNIQUE (name);

ALTER TABLE Faculty
ADD CONSTRAINT faculty_name_color UNIQUE (name, color);