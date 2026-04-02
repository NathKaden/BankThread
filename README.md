# BankThread

Mini projet Java simulant le fonctionnement d'une banque en environnement **multithread**.

---

## Objectif

Mettre en pratique les concepts de concurrence Java à travers une simulation bancaire réaliste : virements, file d'attente de clients, guichets parallèles, surveillance en temps réel et alarmes de découvert.

---

## Structure du projet

```
banque/
├── model/
│   ├── Compte.java         # Compte bancaire thread-safe
│   └── Client.java         # Client avec une opération à exécuter
├── queue/
│   └── FileClients.java    # File d'attente (BlockingQueue)
├── service/
│   ├── Guichet.java        # Guichet bancaire (Runnable)
│   └── Virement.java       # Transfert entre comptes avec anti-deadlock
├── monitoring/
│   ├── SurveillanceThread.java  # Supervision en temps réel
│   ├── AlerteDecouvert.java     # Alarme si compte en négatif
│   ├── Historique.java          # Journal des opérations
│   └── Operation.java           # Modèle d'une opération
├── TestUs4.java            # Test virements concurrents
├── TestUs5.java            # Test file d'attente + monitoring
├── Main.java               # Simulation complète
```

---

## Répartition des User Stories

### Comptes et Virements (Nadia)

US-01 : Compte thread-safe
US-04 : Virement sans deadlock

### File & Guichets (Nathan)

US-02 : File d’attente des clients
US-03 : Pool de guichets

### Surveillance & DAB (JULIEN)

US-06 : Alarme de découvert
US-07 : Limite simultanée aux DAB

### Dashboard & Tests (Juliano)
US-05 : Tableau de bord temps réel
US-08 : Historique des opérations
US-09 : Simulation de charge

---

## Concepts clés utilisés

| Concept | Utilisation |
|---|---|
| `synchronized` | Protection des accès concurrents aux comptes |
| `BlockingQueue` | File d'attente thread-safe des clients |
| `ExecutorService` | Pool de threads pour les guichets |
| `volatile` | Visibilité du flag `running` entre threads |
| `wait()` / `notifyAll()` | Notification passive lors d'un découvert |
| Thread daemon | Monitoring arrêté automatiquement en fin de programme |
| `identityHashCode` | Ordre de verrouillage fixe pour éviter les deadlocks |

---

## Lancer les tests

```bash
# Simulation complète
java -cp out banque.Main
```

---

## Invariant vérifié

Dans `Main.java`, la somme des soldes est vérifiée avant et après toutes les opérations :

```
Somme initiale = 5000.0
Somme finale   = 5000.0
INVARIANT OK ✓
```

Un résultat différent indique une race condition dans la gestion des comptes.