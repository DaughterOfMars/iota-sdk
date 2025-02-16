from iota_sdk import Wallet, StrongholdSecretManager, CoinType
from dotenv import load_dotenv
import json
import os
import sys

load_dotenv()

# This example creates a new database and account

node_url = os.environ.get('NODE_URL', 'https://api.testnet.shimmer.network')
client_options = {
    'nodes': [node_url],
}

# Shimmer coin type
coin_type = CoinType.SHIMMER

if 'STRONGHOLD_PASSWORD' not in os.environ:
    raise Exception(".env STRONGHOLD_PASSWORD is undefined, see .env.example")

secret_manager = StrongholdSecretManager(
    "wallet.stronghold", os.environ['STRONGHOLD_PASSWORD'])

wallet = Wallet('./alice-database', client_options, coin_type, secret_manager)

if 'NON_SECURE_USE_OF_DEVELOPMENT_MNEMONIC_1' not in os.environ:
    raise Exception(".env mnemonic is undefined, see .env.example")

# Store the mnemonic in the Stronghold snapshot, this only needs to be done once
wallet.store_mnemonic(
    os.environ['NON_SECURE_USE_OF_DEVELOPMENT_MNEMONIC_1'])

account = wallet.create_account('Alice')
print("Account created: ", account['alias'])
