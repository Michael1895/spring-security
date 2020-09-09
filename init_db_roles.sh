export PGUSER="$POSTGRES_USER"
#uuid generation: cat /proc/sys/kernel/random/uuid
# remove dashes :
# uuid="$(echo "$(cat /proc/sys/kernel/random/uuid)" | sed 's/-//g')"

"${psql[@]}" --dbname="$POSTGRES_DB" <<-'EOSQL'
		CREATE TABLE IF NOT EXISTS roles (
        id uuid NOT NULL CONSTRAINT roles_pkey PRIMARY KEY,
        name varchar(20)
    );
    INSERT INTO roles(id, name) VALUES('15dcc212-7a86-4452-82b4-d7e696c51dca','ROLE_ADMIN') ON CONFLICT DO NOTHING;
    INSERT INTO roles(id, name) VALUES('71ee9e8e-422a-4488-b9f1-98a4c9c08607', 'ROLE_CREATOR') ON CONFLICT DO NOTHING;
    INSERT INTO roles(id, name) VALUES('be254632-e99a-4604-a777-d6c294e2aae6', 'ROLE_USER') ON CONFLICT DO NOTHING;
EOSQL
