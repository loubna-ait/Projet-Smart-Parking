# Compte Rendu du Projet : Smart Parking System

## 1. Présentation du Projet
Ce projet est un système de gestion de stationnement intelligent développé en **Java SE** avec **JavaFX** pour l'interface graphique et **Hibernate ORM** (mapping XML) pour la persistance des données.

## 2. Objectifs Atteints
Nous avons implémenté les fonctionnalités clés suivantes :

### A. Gestion de la Connexion (Authentification)
- Système de login robuste avec détection automatique du rôle (Admin ou Conducteur).
- Recherche d'email insensible à la casse et suppression des espaces superflus.
- Vérification sécurisée du mot de passe.
- **Auto-initialisation** : Création automatique des comptes par défaut (`loubnaaithra@gmail.com` et `123`) si la base est vide.

### B. Dashboard Administrateur
- Vue d'ensemble des parkings, places et capteurs IoT.
- Gestion CRUD des parkings (Ajouter, Modifier, Supprimer).
- **Indicateur de Capacité** : Affichage dynamique d'une colonne **"Places Libres"** calculée en temps réel.

### C. Gestion des Réservations (Conducteur)
- **Réservation de place** : Un conducteur peut sélectionner une place "Libre" et la réserver. Cela crée un enregistrement dans la table `Reservation` et bloque la place.
- **Anti-Doublon** : Le système empêche un conducteur de posséder plusieurs réservations actives sur la même place.
- **Annulation** : Possibilité pour le conducteur de libérer une place réservée.

### D. Améliorations de l'Interface (UX)
- Remplacement des valeurs techniques (true/false) par des termes clairs (**Libre**, **Occupée**, **Opérationnel**, **En panne**).
- **Personnalisation** : Les places réservées par l'utilisateur connecté s'affichent en **rouge et gras** avec le texte **"Ma Réservation"**.

## 3. Architecture Technique
- **Backend** : Hibernate 5+, MySQL 8.
- **Frontend** : JavaFX 17+, FXML, CSS personnalisé.
- **Entities** : Système d'héritage Hibernate (Joined-Subclass) pour `Utilisateur`, `Administrateur` et `Conducteur`.
- **Logic** : `DatabaseSeeder` pour garantir la présence des comptes de test.

## 4. Identifiants de Test
| Rôle | Email | Mot de passe |
| :--- | :--- | :--- |
| **Conducteur** | `loubnaaithra@gmail.com` | `123` |
| **Admin** | `hassan.alami@smartparking.ma` | `admin123` |

## 5. Conclusion
Le projet dispose désormais d'une base solide et fonctionnelle, couvrant tout le cycle de vie d'une place de parking avec une interface adaptée à chaque utilisateur.
