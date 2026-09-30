DROP USER solar CASCADE;

CREATE USER solar IDENTIFIED BY nilo;

-- Accorder les privilèges nécessaires
GRANT CREATE SESSION TO solar;
GRANT ALL PRIVILEGES TO solar;

-- Connexion à la base de données
CONNECT solar/nilo;

-- Création des tables

CREATE TABLE typeBatterie (
    id NUMBER PRIMARY KEY,
    name VARCHAR2(100) NOT NULL,
    desce VARCHAR2(255),
    vraiCapacite NUMBER NOT NULL,
    prixParW NUMBER NOT NULL
);

CREATE TABLE batteries (
    id NUMBER PRIMARY KEY,
    idType NUMBER REFERENCES typeBatterie(id),
    capacite NUMBER NOT NULL
);

CREATE TABLE panneaux (
    id NUMBER PRIMARY KEY,
    prixParW NUMBER NOT NULL
);

CREATE TABLE devis (
    id NUMBER PRIMARY KEY,
    estConfirme CHAR(1) CHECK (estConfirme IN ('Y', 'N'))
);

CREATE TABLE listeMateriauxDevis (
    id NUMBER PRIMARY KEY,
    idDevis NUMBER REFERENCES devis(id),
    materiel VARCHAR2(100) NOT NULL,
    puissance NUMBER NOT NULL,
    heureDebutUtilisation NUMBER NOT NULL CHECK (heureDebutUtilisation BETWEEN 0 AND 24),
    heureFinUtilisation NUMBER NOT NULL CHECK (heureFinUtilisation BETWEEN 0 AND 24)
);

CREATE TABLE saison (
    id NUMBER PRIMARY KEY,
    nom VARCHAR2(100) NOT NULL,
    desce VARCHAR2(255),
    jourMoisDebut VARCHAR2(5) NOT NULL,
    jourMoisFin VARCHAR2(5) NOT NULL
);

CREATE TABLE puissanceSoleil (
    id NUMBER PRIMARY KEY,
    idSaison NUMBER REFERENCES saison(id),
    heureDebut NUMBER NOT NULL CHECK (heureDebut BETWEEN 0 AND 24),
    heureFin NUMBER NOT NULL CHECK (heureFin BETWEEN 0 AND 24),
    pourcentage NUMBER NOT NULL CHECK (pourcentage BETWEEN 0 AND 100)
);

CREATE TABLE confirmationDevis (
    id NUMBER PRIMARY KEY,
    idDevis NUMBER REFERENCES devis(id),
    adresse VARCHAR2(255) NOT NULL,
    nom VARCHAR2(100) NOT NULL
);

-- Création des séquences

CREATE SEQUENCE seq_typeBatterie START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_batteries START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_panneaux START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_devis START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_listeMateriauxDevis START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_saison START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_puissanceSoleil START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_confirmationDevis START WITH 1 INCREMENT BY 1;

-- Création des triggers pour auto-incrémenter les IDs

CREATE OR REPLACE TRIGGER trg_typeBatterie_id
BEFORE INSERT ON typeBatterie
FOR EACH ROW
BEGIN
    :NEW.id := seq_typeBatterie.NEXTVAL;
END;
/

CREATE OR REPLACE TRIGGER trg_batteries_id
BEFORE INSERT ON batteries
FOR EACH ROW
BEGIN
    :NEW.id := seq_batteries.NEXTVAL;
END;
/

CREATE OR REPLACE TRIGGER trg_panneaux_id
BEFORE INSERT ON panneaux
FOR EACH ROW
BEGIN
    :NEW.id := seq_panneaux.NEXTVAL;
END;
/

CREATE OR REPLACE TRIGGER trg_devis_id
BEFORE INSERT ON devis
FOR EACH ROW
BEGIN
    :NEW.id := seq_devis.NEXTVAL;
END;
/

CREATE OR REPLACE TRIGGER trg_listeMateriauxDevis_id
BEFORE INSERT ON listeMateriauxDevis
FOR EACH ROW
BEGIN
    :NEW.id := seq_listeMateriauxDevis.NEXTVAL;
END;
/

CREATE OR REPLACE TRIGGER trg_saison_id
BEFORE INSERT ON saison
FOR EACH ROW
BEGIN
    :NEW.id := seq_saison.NEXTVAL;
END;
/

CREATE OR REPLACE TRIGGER trg_puissanceSoleil_id
BEFORE INSERT ON puissanceSoleil
FOR EACH ROW
BEGIN
    :NEW.id := seq_puissanceSoleil.NEXTVAL;
END;
/

CREATE OR REPLACE TRIGGER trg_confirmationDevis_id
BEFORE INSERT ON confirmationDevis
FOR EACH ROW
BEGIN
    :NEW.id := seq_confirmationDevis.NEXTVAL;
END;
/

-- Vue pour les devis

CREATE OR REPLACE VIEW vue_devis_confirmation AS
SELECT 
    d.id AS id,
    d.estConfirme,
    c.adresse,
    c.nom
FROM 
    devis d
LEFT JOIN 
    confirmationDevis c ON d.id = c.idDevis;

-- Insertion des données d'exemple

INSERT INTO typeBatterie (name, desce, vraiCapacite, prixParW) VALUES ('Batterie au plomb', 'Batterie standard pour stockage solaire', 0.8, 10);
INSERT INTO typeBatterie (name, desce, vraiCapacite, prixParW) VALUES ('Batterie lithium-ion', 'Batterie avec meilleure durabilité et performance', 0.7, 10);
INSERT INTO typeBatterie (name, desce, vraiCapacite, prixParW) VALUES ('Batterie gel', 'Batterie performante', 0.4, 10);

INSERT INTO batteries (idType, capacite) VALUES (1, 120);
INSERT INTO batteries (idType, capacite) VALUES (1, 150);
INSERT INTO batteries (idType, capacite) VALUES (2, 250);
INSERT INTO batteries (idType, capacite) VALUES (2, 300);

INSERT INTO panneaux (prixParW) VALUES (10);

INSERT INTO devis (estConfirme) VALUES ('N');
INSERT INTO devis (estConfirme) VALUES ('Y');
INSERT INTO devis (estConfirme) VALUES ('N');

-- Insertion de données supplémentaires dans la table listeMateriauxDevis
INSERT INTO listeMateriauxDevis (idDevis, materiel, puissance, heureDebutUtilisation, heureFinUtilisation) VALUES (1, 'Ventilateur', 50, 12, 18);
INSERT INTO listeMateriauxDevis (idDevis, materiel, puissance, heureDebutUtilisation, heureFinUtilisation) VALUES (1, 'Ordinateur portable', 60, 19, 23);
INSERT INTO listeMateriauxDevis (idDevis, materiel, puissance, heureDebutUtilisation, heureFinUtilisation) VALUES (1, 'Éclairage extérieur', 20, 18, 23);
INSERT INTO listeMateriauxDevis (idDevis, materiel, puissance, heureDebutUtilisation, heureFinUtilisation) VALUES (1, 'Chargeur de téléphone', 5, 8, 22);

INSERT INTO listeMateriauxDevis (idDevis, materiel, puissance, heureDebutUtilisation, heureFinUtilisation) VALUES (2, 'Télévision', 50, 18, 22);
INSERT INTO listeMateriauxDevis (idDevis, materiel, puissance, heureDebutUtilisation, heureFinUtilisation) VALUES (2, 'Radio', 15, 6, 9);
INSERT INTO listeMateriauxDevis (idDevis, materiel, puissance, heureDebutUtilisation, heureFinUtilisation) VALUES (2, 'Éclairage intérieur', 10, 18, 23);
INSERT INTO listeMateriauxDevis (idDevis, materiel, puissance, heureDebutUtilisation, heureFinUtilisation) VALUES (2, 'Pompe à eau', 500, 5, 7);

INSERT INTO listeMateriauxDevis (idDevis, materiel, puissance, heureDebutUtilisation, heureFinUtilisation) VALUES (3, 'Réfrigérateur', 150, 0, 23);
INSERT INTO listeMateriauxDevis (idDevis, materiel, puissance, heureDebutUtilisation, heureFinUtilisation) VALUES (3, 'Cuisinière électrique', 1000, 6, 7);
INSERT INTO listeMateriauxDevis (idDevis, materiel, puissance, heureDebutUtilisation, heureFinUtilisation) VALUES (3, 'Mixeur', 300, 8, 9);
INSERT INTO listeMateriauxDevis (idDevis, materiel, puissance, heureDebutUtilisation, heureFinUtilisation) VALUES (3, 'Machine à laver', 500, 9, 11);

INSERT INTO listeMateriauxDevis (idDevis, materiel, puissance, heureDebutUtilisation, heureFinUtilisation) VALUES (3, 'Climatiseur', 2000, 12, 15);
INSERT INTO listeMateriauxDevis (idDevis, materiel, puissance, heureDebutUtilisation, heureFinUtilisation) VALUES (3, 'Ordinateur de bureau', 200, 9, 17);
INSERT INTO listeMateriauxDevis (idDevis, materiel, puissance, heureDebutUtilisation, heureFinUtilisation) VALUES (3, 'Chargeur de batterie', 20, 10, 14);
INSERT INTO listeMateriauxDevis (idDevis, materiel, puissance, heureDebutUtilisation, heureFinUtilisation) VALUES (3, 'Éclairage LED', 8, 18, 23);

INSERT INTO saison (nom, desce, jourMoisDebut, jourMoisFin) VALUES ('Saison sèche', 'Période de faible pluie', '05-01', '10-31');
INSERT INTO saison (nom, desce, jourMoisDebut, jourMoisFin) VALUES ('Saison humide', 'Période de pluie abondante', '11-01', '04-30');

INSERT INTO puissanceSoleil (idSaison, heureDebut, heureFin, pourcentage) VALUES (1, 6, 10, 20);
INSERT INTO puissanceSoleil (idSaison, heureDebut, heureFin, pourcentage) VALUES (1, 10, 13, 60);
INSERT INTO puissanceSoleil (idSaison, heureDebut, heureFin, pourcentage) VALUES (1, 13, 16, 40);
INSERT INTO puissanceSoleil (idSaison, heureDebut, heureFin, pourcentage) VALUES (1, 16, 18, 10);
INSERT INTO puissanceSoleil (idSaison, heureDebut, heureFin, pourcentage) VALUES (1, 18, 6, 0);

INSERT INTO puissanceSoleil (idSaison, heureDebut, heureFin, pourcentage) VALUES (2, 6, 10, 30);
INSERT INTO puissanceSoleil (idSaison, heureDebut, heureFin, pourcentage) VALUES (2, 10, 13, 70);
INSERT INTO puissanceSoleil (idSaison, heureDebut, heureFin, pourcentage) VALUES (2, 13, 16, 50);
INSERT INTO puissanceSoleil (idSaison, heureDebut, heureFin, pourcentage) VALUES (2, 16, 18, 20);
INSERT INTO puissanceSoleil (idSaison, heureDebut, heureFin, pourcentage) VALUES (2, 18, 6, 0);

INSERT INTO confirmationDevis (idDevis, adresse, nom) VALUES (1, 'Antananarivo, Lot IVG 32', 'Rasoa');
INSERT INTO confirmationDevis (idDevis, adresse, nom) VALUES (2, 'Fianarantsoa, Avenue de la Libération', 'Rakoto');
INSERT INTO confirmationDevis (idDevis, adresse, nom) VALUES (3, 'Toamasina, Rue de la Paix', 'Randria');

-- SELECT MIN(pourcentage) as minPourcentage FROM puissanceSoleil;