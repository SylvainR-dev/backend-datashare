# MAINTENANCE.md — DataShare

## Procédures de mise à jour des dépendances

### Backend (Maven)

| Fréquence | Action |
|---|---|
| Mensuelle | Vérifier les mises à jour disponibles avec `mvn versions:display-dependency-updates` |
| Mensuelle | Vérifier les mises à jour des plugins avec `mvn versions:display-plugin-updates` |
| Trimestrielle | Mettre à jour Spring Boot vers la dernière version stable |

**Commandes utiles :**

```bash
# Vérifier les dépendances obsolètes
mvn versions:display-dependency-updates

# Vérifier les plugins obsolètes
mvn versions:display-plugin-updates

# Scan de sécurité
trivy fs . --scanners vuln
```

### Frontend (npm)

| Fréquence | Action |
|---|---|
| Mensuelle | Vérifier les mises à jour avec `npm outdated` |
| Mensuelle | Scan de sécurité avec `npm audit` |
| Trimestrielle | Mettre à jour Angular vers la dernière version LTS |

**Commandes utiles :**

```bash
# Vérifier les dépendances obsolètes
npm outdated

# Scan de sécurité
npm audit

# Corriger automatiquement les vulnérabilités mineures
npm audit fix
```

## Risques liés aux mises à jour

| Risque | Probabilité | Impact | Mitigation |
|---|---|---|---|
| Régression après montée de version Spring Boot | Moyenne | Élevé | Relancer la suite de tests complète avant déploiement |
| Breaking change Angular | Faible | Élevé | Consulter le changelog Angular et tester en local |
| Vulnérabilité non corrigée | Moyenne | Élevé | Scan trivy et npm audit mensuel |
| Incompatibilité JWT après mise à jour | Faible | Moyen | Vérifier la compatibilité de jjwt avec la nouvelle version |

## Procédure de mise à jour

1. Créer une branche dédiée `chore/update-dependencies`
2. Appliquer les mises à jour
3. Relancer tous les tests : `mvn test` / `ng test`
4. Vérifier le scan de sécurité : `trivy fs .` / `npm audit`
5. Tester manuellement les fonctionnalités critiques (upload, download, login)
6. Merger sur la branche principale après validation