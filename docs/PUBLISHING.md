# Publishing

Builds run on GitHub Actions and are uploaded over FTPS to Forpsi shared hosting. There is no build
server to maintain — `.github/workflows/publish.yml` is the whole of it.

| Host | What it is |
|---|---|
| `repo.drawethree.dev` | Maven repository — `/releases` and `/snapshots` |
| `ci.drawethree.dev/x-prison` | Build listing and jar downloads |
| `javadocs.drawethree.dev/x-prison` | Browsable API docs, tagged releases only |

**The full runbook lives in [X-WardenAPI/docs/PUBLISHING.md](https://github.com/Drawethree/X-WardenAPI/blob/master/docs/PUBLISHING.md)** — hosting layout,
DNS, the FTP geo-restriction, why the metadata is seeded before deploying, and troubleshooting.
This file only records what is specific to X-PrisonAPI.

## Specific to this project

- **JDK 21.** Nexo 1.25.0 ships Java 21 bytecode (class-file v65), so the compiler must run on JDK 21+ to read its API classes. The build still targets release 17, keeping the published jar consumable by Java 17 projects. Dropping the workflow to 17 breaks the build outright.
- **Slug `x-prison`.** The build listing publishes to `ci.drawethree.dev/x-prison/`, never the
  root. Run numbers are per repository, so an unslugged path would collide with the other APIs.
- **`finalName` is `X-PrisonAPI ${project.version}`** — with a space. That only affects `target/`; Maven deploys
  by coordinates, so the repository tree is unaffected. The workflow still has to glob
  `target/*.jar` rather than match the artifact id, and it republishes the jar to the build page
  under the coordinate name so no download URL contains `%20`.
- **The landing page at `ci.drawethree.dev/` is not owned by this repository.** It lives in
  X-WardenAPI as `ci/root-index.html`. Adding or renaming a project means editing it there.

## Publishing

**A snapshot** — push to `master`. Deploys `1.9-SNAPSHOT` to `/snapshots` and adds a row to the
build listing.

**A release** — tag it:

```bash
git tag v1.9
git push origin v1.9
```

The tag is the source of truth: the workflow stamps the version into the pom before building, so
the artifact and the tag cannot disagree. It deploys to `/releases`, publishes a GitHub release
with the jars attached, and marks the build green on the listing page.

Bump the pom to the next `-SNAPSHOT` afterwards so master snapshots stop colliding with the
version you just shipped.

## Javadoc

Published to `javadocs.drawethree.dev/x-prison/` on **tagged releases only** — never on snapshot
pushes. A javadoc site is hundreds of files and therefore hundreds of FTP round trips; that is
fine a few times a year and pointless on every commit, since IDE users already get the
`-javadoc.jar` from the Maven repository on every publish.

Each version keeps its own directory, so `/x-prison/1.9/` still resolves after 2.0 ships and wiki
links do not rot. The project root is a small redirect to the newest release. Old versions prune
to the newest five.

The landing page at `javadocs.drawethree.dev/` lives in X-WardenAPI as `javadocs/root-index.html`,
the same way the ci one does.

## Secrets

Set on this repository under `Settings → Secrets and variables → Actions`:

| Secret | Where to find it |
|---|---|
| `FTP_HOST` | Forpsi panel, `Webhosting -> drawethree.dev -> FTP`, the **server** field |
| `FTP_USERNAME` | same page, the **login** field |
| `FTP_PASSWORD` | the FTP password |

This repository is public, so the values are deliberately not written down here.

If the upload fails with `530 Login authentication failed`, check the FTP allowlist before the
password — Forpsi restricts FTP by country and GitHub's runners are in the United States. That is
documented in full in the X-WardenAPI runbook.
