// MongoDB initialization script
db = db.getSiblingDB('marketplace_product');
db.createUser({
    user: 'mongo',
    pwd: 'mongo123',
    roles: [
        { role: 'readWrite', db: 'marketplace_product' }
    ]
});
db.createCollection('products');
print('Initialized marketplace_product database');
