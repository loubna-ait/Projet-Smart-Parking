CREATE DATABASE IF NOT EXISTS smart_parking;
USE smart_parking;

CREATE TABLE IF NOT EXISTS Utilisateur (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL -- 'ADMIN', 'CONDUCTEUR'
);

CREATE TABLE IF NOT EXISTS Parking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    adresse VARCHAR(255) NOT NULL,
    latitude DOUBLE,
    longitude DOUBLE,
    capacite_totale INT NOT NULL
);

CREATE TABLE IF NOT EXISTS PlaceStationnement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(50) NOT NULL,
    etage INT,
    type VARCHAR(50), -- 'STANDARD', 'HANDICAP', 'ELECTRIQUE'
    est_disponible BOOLEAN DEFAULT TRUE,
    parking_id BIGINT,
    FOREIGN KEY (parking_id) REFERENCES Parking(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS TarifDynamique (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type_jour VARCHAR(50), -- 'SEMAINE', 'WEEKEND', 'FERIE'
    heure_debut INT,
    heure_fin INT,
    tarif_horaire DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS Reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_debut DATETIME NOT NULL,
    date_fin DATETIME NOT NULL,
    cout_estime DECIMAL(10, 2),
    statut VARCHAR(50), -- 'EN_ATTENTE', 'CONFIRMEE', 'TERMINEE', 'ANNULEE'
    conducteur_id BIGINT,
    place_id BIGINT,
    FOREIGN KEY (conducteur_id) REFERENCES Utilisateur(id),
    FOREIGN KEY (place_id) REFERENCES PlaceStationnement(id)
);

CREATE TABLE IF NOT EXISTS Paiement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date_paiement DATETIME DEFAULT CURRENT_TIMESTAMP,
    montant DECIMAL(10, 2) NOT NULL,
    moyen_paiement VARCHAR(50),
    statut VARCHAR(50), -- 'VALIDE', 'ECHOUE'
    reservation_id BIGINT UNIQUE,
    FOREIGN KEY (reservation_id) REFERENCES Reservation(id)
);

CREATE TABLE IF NOT EXISTS CapteurIoT (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ip_address VARCHAR(50),
    etat_batterie INT,
    statut_operationnel BOOLEAN DEFAULT TRUE,
    place_id BIGINT UNIQUE,
    FOREIGN KEY (place_id) REFERENCES PlaceStationnement(id)
);

-- Separate tables if joined strategy is used later, or just managed via Utilisateur
CREATE TABLE IF NOT EXISTS Administrateur (
    id BIGINT PRIMARY KEY,
    departement VARCHAR(255),
    FOREIGN KEY (id) REFERENCES Utilisateur(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Conducteur (
    id BIGINT PRIMARY KEY,
    num_permis VARCHAR(255),
    FOREIGN KEY (id) REFERENCES Utilisateur(id) ON DELETE CASCADE
);

-- Insertion de Loubna (Conducteur par défaut)
INSERT INTO Utilisateur (nom, prenom, email, mot_de_passe, role) 
VALUES ('loubna', 'aithra', 'loubnaaithra@gmail.com', '123', 'CONDUCTEUR');
SET @last_id = LAST_INSERT_ID();
INSERT INTO Conducteur (id, num_permis) VALUES (@last_id, 'LX987654');
