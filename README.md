# ⚡ Calcul Parallèle de π — HPC · M2 SRT / GLSI

**ESP Dakar · UCAD — Année universitaire 2025–2026**  
**Auteur : Ibrahima FALL**

Implémentation Java de trois stratégies de scheduling parallèle appliquées au calcul de π par la formule de Leibniz sur N = 2 000 000 000 itérations.

---

## 📁 Structure du projet

```
src/
├── sequentiel/
│   └── CalculPI.java                          # Version séquentielle (référence)
├── parallel/
│   ├── CalculPI_Parallel.java                 # Parallèle basique (2 threads fixes)
│   ├── CalculPI_Parallel_StaticSched.java     # Static Scheduling
│   ├── CalculPI_Parallel_StaticSelf.java      # Self Scheduling
│   └── CalculPI_Parallel_StaticGuidedSelf.java # Guided Self Scheduling
└── schedule/
    ├── staticSchedule.java                    # Algorithme Static Scheduling
    ├── selfSchedule.java                      # Algorithme Self Scheduling
    ├── guidedSelfSchedule.java                # Algorithme Guided Self Scheduling
    └── main.java                              # Test des 3 schedulers
```

---

## 🧮 Formule utilisée

$$\frac{\pi}{4} = \sum_{i=0}^{N-1} \frac{(-1)^i}{2i+1}, \quad N = 2\,000\,000\,000$$

---

## 🧩 Stratégies implémentées

### 1. Static Scheduling
Les N itérations sont découpées en `p` blocs **égaux et fixes** avant le démarrage.  
✅ Zéro synchronisation dynamique — optimal pour charges uniformes (DoAll).

### 2. Self Scheduling
Chaque thread réclame dynamiquement un bloc de taille `groupSize` via `loopGetRange()`.  
✅ Bon équilibrage de charge — surcoût de synchronisation proportionnel à N/groupSize.

### 3. Guided Self Scheduling
Blocs de taille **décroissante** : grands au début (N/P), réduits jusqu'à `minSize`.  
✅ Compromis optimal entre synchronisation et équilibrage.

---

## 📊 Résultats de performance (T₁ = 21 s)

### Static Scheduling

| Threads | Temps | Accélération | Efficacité |
|---------|-------|-------------|------------|
| 2       | 9 s   | 2.33×       | 116.7 %    |
| 4       | 16 s  | 1.31×       | 32.8 %     |
| 6       | 12 s  | 1.75×       | 29.2 %     |
| 8       | 10 s  | 2.10×       | 26.3 %     |

### Self Scheduling (4 cœurs)

| groupSize   | Temps | Accélération | Efficacité |
|-------------|-------|-------------|------------|
| 2 000 000 000 | 26 s | 0.81×      | 20.2 %     |
| 500 000 000 | 5 s   | 4.20×       | 105.0 %    |
| 10 000 000  | 6 s   | 3.50×       | 87.5 %     |
| 1 000 000   | 6 s   | 3.50×       | 87.5 %     |

---

## 🚀 Compilation et exécution

```bash
# Compiler tout le projet depuis la racine
javac -d out src/schedule/*.java src/sequentiel/*.java src/parallel/*.java

# Exécution séquentielle
java -cp out sequentiel.CalculPI

# Static Scheduling
java -cp out parallel.CalculPI_Parallel_StaticSched

# Self Scheduling
java -cp out parallel.CalculPI_Parallel_StaticSelf

# Guided Self Scheduling
java -cp out parallel.CalculPI_Parallel_StaticGuidedSelf

# Test des 3 schedulers (petite boucle)
java -cp out schedule.main
```

---

## ⚙️ Paramètres à modifier

Dans chaque fichier `parallel/` :

| Paramètre | Rôle |
|-----------|------|
| `numThreads` | Nombre de threads (2, 4, 6, 8) |
| `groupSize` | Taille des blocs (Self Scheduling) |
| `minSize` | Taille minimale des blocs (Guided Self) |

---

## 👤 Auteur

**Ibrahima FALL** — Master 2 SRT / GLSI  
ESP Dakar · UCAD · 2025–2026  
Cours : Calcul Parallèle et High Performance Computing  
Professeur : Dr. Abdourahmane SENGHOR
