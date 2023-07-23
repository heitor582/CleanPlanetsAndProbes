CREATE TABLE probes(
  id SERIAL NOT NULL PRIMARY KEY,
  cord_x INTEGER NOT NULL,
  cord_y INTEGER NOT NULL,
  direction VARCHAR(5) NOT NULL,
  name varchar(255) NOT NULL,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  planet_id SERIAL NOT NULL,
   CONSTRAINT fk_id_planet FOREIGN KEY (planet_id)
   REFERENCES planets(id)
   ON DELETE CASCADE
);