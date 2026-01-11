# Rapport de Projet : Smart Parking System
# 4IIR EMSI 2025-2026

## 1. Page de Garde
*   **Logo de l'établissement :** EMSI
*   **Titre du projet :** Smart Parking System - Gestion de Stationnement Intégré (Hibernate & JavaFX)
*   **Intitulé du module :** Java Avancé / Programmation Orientée Objet
*   **Réalisé par :** **Loubna Ait-Hra**
*   **Classe :** 4IIR
*   **Centre :** EMSI MOULAY YOUSSEF
*   **Année Universitaire :** 2025-2026

---

## 2. Remerciements
Nous tenons à remercier notre encadrant pour ses précieux conseils tout au long de ce module de Java Avancé, ainsi que l'administration de l'EMSI pour l'accès aux ressources nécessaires à la réalisation de ce projet.

---

## 3. Table des Matières
1. [Introduction Générale](#4-introduction-générale)
2. [Analyse et Conception](#5-partie-i--analyse-et-conception)
    * [5.1 Spécification des besoins](#51-spécification-des-besoins)
    * [5.2 Conception UML](#52-conception-uml-diagramme-de-classes)
    * [5.3 Conception de la Base de Données](#53-conception-de-la-base-de-données)
3. [Environnement Technique](#6-partie-ii--environnement-technique)
4. [Architecture et Implémentation](#7-partie-iii--architecture-et-implémentation)
    * [7.1 Architecture logicielle (MVC)](#71-architecture-logicielle-mvc)
    * [7.2 Design Patterns](#72-design-patterns)
    * [7.3 Extraits de code clés](#73-extraits-de-code-clés)
5. [Interface Utilisateur et Tests](#8-partie-iv--interface-utilisateur-et-tests)
    * [8.1 Présentation des interfaces](#81-présentation-des-interfaces-captures-décran)
    * [8.2 Scénarios de Test](#82-scénarios-de-test)
6. [Conclusion et Perspectives](#9-conclusion-et-perspectives)
7. [Webographie](#10-webographie--bibliographie)

---

## 4. Introduction Générale
*   **Contexte du projet :** Ce projet s'inscrit dans le cadre du module Java Avancé. Il vise à simuler un système moderne de gestion de parking urbain.
*   **Problématique :** La gestion manuelle des parkings entraîne des pertes de temps, une mauvaise optimisation de l'espace et une difficulté pour les conducteurs à trouver des places disponibles en temps réel.
*   **Objectifs :** 
    *   Authentification sécurisée avec rôles spécifiques (Admin/Conducteur).
    *   Gestion CRUD des infrastructures de parking.
    *   Système de réservation en temps réel avec suivi de disponibilité.
    *   Dashboard analytique pour l'administrateur (Places libres).

---

## 5. Partie I : Analyse et Conception

### 5.1 Spécification des besoins
*   **Besoins Fonctionnels :** 
    *   Se connecter au système.
    *   Visualiser la liste des parkings et leur capacité libre.
    *   Réserver/Annuler une place (Conducteur).
    *   Gérer les parkings et capteurs (Admin).
*   **Besoins Non-Fonctionnels :** 
    *   Persistance des données via Hibernate.
    *   Interface graphique intuitive sous JavaFX.
    *   Gestion automatique des comptes de test au démarrage.

### 5.2 Conception UML (Diagramme de Classes)

![Diagramme de Classe](file:///c:/xampp/mysql/data/smart_parking_db/smart-parking-orm-xml/diagramme_classe.png)

**Explication Générale :**
Le diagramme de classe structure le système autour de quatre piliers majeurs :
1.  **Gestion des Utilisateurs** : Un système d’héritage lie `Utilisateur` à `Administrateur` et `Conducteur`, permettant une gestion centralisée des accès.
2.  **Infrastructure de Parking** : La classe `Parking` agrège plusieurs `PlaceStationnement`. Chaque place est associée à un `CapteurIoT` pour le suivi en temps réel et à un `TarifDynamique` pour le calcul automatique des coûts.
3.  **Processus de Réservation** : L'interface `IReservable` garantit que les places respectent le cycle de vie (vérifier, réserver, libérer). La classe `Reservation` fait le pont entre le `Conducteur` et la `PlaceStationnement`.
4.  **Finalisation** : Chaque `Reservation` aboutit à un `Paiement` unique, assurant la traçabilité financière des transactions.

### 5.3 Conception de la Base de Données
*   **MLD :** Tables `Utilisateur`, `Administrateur`, `Conducteur`, `Parking`, `PlaceStationnement`, `Reservation`, `Paiement`, `CapteurIoT`.
*   **PK/FK :** Utilisation de l'Identity pour les IDs et de contraintes `ON DELETE CASCADE` pour les sous-classes.

---

## 6. Partie II : Environnement Technique
*   **Langage :** Java 17 (JDK).
*   **IDE :** IntelliJ IDEA / VS Code.
*   **Gestion de projet :** Maven (Gestion des dépendances Hibernate, MySQL Connector, JavaFX).
*   **SGBD :** MySQL 8.0.
*   **Bibliothèques tierces :** 
    *   Hibernate ORM (Persistance).
    *   JavaFX (Interface graphique).
    *   MySQL Connector (JDBC).

---

## 7. Partie III : Architecture et Implémentation

### 7.1 Architecture logicielle (MVC)
Organisation des packages :
*   `ma.emsi.entities` : Entités persistantes mappées par Hibernate (XML).
*   `ma.emsi.ui` : Contrôleurs JavaFX (`LoginController`, `MainController`).
*   `ma.emsi.util` : Utilitaires (`HibernateUtil`, `DatabaseSeeder`).
*   `ma.emsi.resources` : Fichiers FXML et CSS.

### 7.2 Design Patterns
*   **Singleton :** `HibernateUtil` garantit une instance unique de `SessionFactory`.
*   **Session-per-request :** Ouverture/fermeture propre des sessions Hibernate dans les contrôleurs.
*   **Data Seeding :** `DatabaseSeeder` assure la cohérence des comptes sans intervention manuelle.

### 7.3 Extraits de code clés
*   **Vérification de rôle personnalisée :**
```java
if (user != null && user.getMotDePasse().equals(password)) {
    String dbRole = user.getRole().toUpperCase();
    App.setCurrentUser(user);
    App.setRoot("MainView");
}
```
*   **Calcul dynamique des places libres (HQL) :**
```java
session.createQuery("select count(p) from PlaceStationnement p where p.parking.id = :pid and p.estDisponible = true")
```

---

## 8. Partie IV : Interface Utilisateur et Tests

### 8.1 Présentation des interfaces (Captures d'écran)

#### A. Interface d'Authentification (Login)
*   **Description** : Premier point d'entrée de l'application permettant d'identifier l'utilisateur et son rôle.
*   **Fonctionnalités** :
    *   Saisie sécurisée de l'email et du mot de passe.
    *   Gestion personnalisée des erreurs (ex: "Utilisateur non trouvé", "Mot de passe incorrect").
    *   Redirection intelligente vers le tableau de bord correspondant au rôle (Admin ou Conducteur).

#### B. Dashboard Administrateur (Gestion Globale)
*   **Description** : Interface dédiée à la supervision complète de l'infrastructure.
*   **Fonctionnalités** :
    *   **Gestion des Parkings** : Tableau interactif avec options d'ajout, de modification et de suppression.
    *   **Indicateur de Disponibilité** : Colonne "Places Libres" recalculée dynamiquement pour chaque site.
    *   **Suivi des Capteurs** : Liste des capteurs IoT avec leur état réel (Opérationnel/En panne).

#### C. Interface Conducteur (Réservation Dynamique)
*   **Description** : Interface fluide centrée sur l'utilisateur final et la mobilité.
*   **Fonctionnalités** :
    *   **Disponibilité des Places** : Affichage clair avec les statuts "Libre" ou "Occupée".
    *   **Réservation Instantanée** : Système permettant de bloquer une place en un clic (statut Persistant).
    *   **Expérience Personnalisée** : Les places réservées par l'utilisateur actuel s'affichent avec le texte **"Ma Réservation"** en rouge pour une visibilité immédiate.
    *   **Annulation** : Possibilité de libérer la place à tout moment pour la rendre à nouveau disponible.

### 8.2 Scénarios de Test
*   **Nominal :** Loubna réserve une place -> Status devient "Ma Réservation".
*   **Erreur :** Tentative de réservation d'une place déjà occupée -> Alerte bloquante.
*   **Restauration :** Annulation d'une réservation -> La place redevient "Libre".

---

## 9. Conclusion et Perspectives
*   **Bilan technique :** Toutes les fonctionnalités Java Avancé (Hibernate, JavaFX, Architecture multi-couches) sont opérationnelles.
*   **Difficultés :** La synchronisation entre les entités Hibernate et l'affichage JavaFX a nécessité une gestion fine des sessions.
*   **Perspectives :** Ajout d'un système de paiement par carte (module `Paiement`) et intégration de notifications en temps réel.

## 10. Webographie / Bibliographie
*   Documentation Hibernate : [hibernate.org](https://hibernate.org/)
*   Tutoriels JavaFX : [openjfx.io](https://openjfx.io/)
*   Cours EMSI Java Avancé.
