# SECURITY.md — DataShare Backend

## Scan de sécurité

Outil utilisé : **Trivy v0.52.2**  
Commande : `trivy fs . --scanners vuln`  
Date du scan : 04/06/2026

## Résultats du scan

| Sévérité | CVE | Librairie | Description | Version corrigée |
|---|---|---|---|---|
| CRITICAL | CVE-2026-22732 | spring-security-web | Security policy bypass et information disclosure | 6.5.9, 7.0.4 |
| HIGH | CVE-2025-41248 | spring-security-core | Authorization bypass | 6.4.10, 6.5.4 |
| HIGH | CVE-2025-41249 | spring-core | Annotation Detection Vulnerability | 6.2.11 |
| MEDIUM | CVE-2026-22751 | spring-security-core | Authentication bypass (race condition) | 6.5.10, 7.0.5 |
| MEDIUM | CVE-2025-41234 | spring-web | Reflected download attack | 6.2.8, 6.1.21 |
| LOW | CVE-2026-22746 | spring-security-core | Timing attack bypass | 6.5.10, 7.0.5 |
| LOW | CVE-2025-22233 | spring-context | Locale conversion vulnerability | 6.2.7, 6.1.20 |
| LOW | CVE-2026-22735 | spring-webmvc | Stream corruption via SSE | 7.0.6, 6.2.17 |

## Analyse des décisions

Toutes les vulnérabilités détectées proviennent de **Spring Framework** et **Spring Security**, 
embarqués transitivement via **Spring Boot 3.4.5**.

### Décisions prises

**Vulnérabilités acceptées (non corrigées)**  
La mise à jour vers Spring Boot 3.5.x pour corriger ces CVE a été évaluée mais non appliquée 
pour les raisons suivantes :
- Le projet est en fin de développement, une montée de version majeure introduit un risque de 
régression sur l'ensemble des fonctionnalités
- Les CVE CRITICAL et HIGH concernent des fonctionnalités non utilisées dans ce projet 
(JdbcOneTimeTokenService, annotations avancées)
- L'application est hébergée sur un réseau local (Raspberry Pi homelab), ce qui limite 
significativement la surface d'exposition

**Action prévue en maintenance**  
La mise à jour de Spring Boot sera effectuée lors du prochain cycle de maintenance, 
conformément aux procédures décrites dans `MAINTENANCE.md`.

## Scan frontend

Outil utilisé : **npm audit**  
Résultat : **0 vulnérabilité détectée**