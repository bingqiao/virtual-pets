db.createUser(
        {
            user: "vpetuser",
            pwd: "letmein",
            roles: [
                {
                    role: "readWrite",
                    db: "vpet"
                }
            ]
        }
);
