// Copyright 2022 IOTA Stiftung
// SPDX-License-Identifier: Apache-2.0

//! Returns all stored receipts for a given migration index by calling `GET /api/core/v2/receipts/{migratedAt}`.
//!
//! `cargo run --example node_api_core_get_receipts_migrated_at --release -- [NODE URL]`

use iota_sdk::client::{Client, Result};

#[tokio::main]
async fn main() -> Result<()> {
    // Take the node URL from command line argument or use one from env as default.
    let node_url = std::env::args().nth(1).unwrap_or_else(|| {
        // This example uses secrets in environment variables for simplicity which should not be done in production.
        dotenvy::dotenv().ok();
        std::env::var("NODE_URL").unwrap()
    });

    // Create a client with that node.
    let client = Client::builder().with_node(&node_url)?.finish().await?;

    // Send the request.
    let receipts = client.get_receipts_migrated_at(1_000_000).await?;

    println!("{receipts:#?}");

    Ok(())
}
