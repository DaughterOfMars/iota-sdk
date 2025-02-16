// Copyright 2022 IOTA Stiftung
// SPDX-License-Identifier: Apache-2.0
package org.iota;

import org.iota.types.AccountAddress;
import org.iota.types.Account;
import org.iota.types.addresses.Address;
import org.iota.types.account_methods.GenerateEd25519Addresses;
import org.iota.types.account_methods.GenerateAddressOptions;
import org.iota.types.account_methods.SetAlias;
import org.iota.types.exceptions.WalletException;
import org.iota.types.ids.account.AccountAlias;
import org.iota.types.ids.account.AccountIndex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WalletTests extends TestSettings {
        @Test
        public void testCreateAccount() throws WalletException {
                System.out.println(wallet.createAccount("Alice"));
        }

        @Test
        public void testCreateAccountsWithSameAlias() throws WalletException {
                System.out.println(wallet.createAccount("Alice"));
                try {
                        System.out.println(wallet.createAccount("Alice"));
                } catch (WalletException expectedException) {
                        ;
                }
        }

        @Test
        public void testGetAccountByAlias() throws WalletException {
                Account a = wallet.createAccount("Alice");
                Account b = wallet.getAccount(new AccountAlias("Alice"));
                assertEquals(a, b);
        }

        @Test
        public void testGetAccountByIndex() throws WalletException {
                Account a = wallet.createAccount("Alice");
                Account b = wallet.getAccount(new AccountIndex(0));
                assertEquals(a, b);
        }

        @Test
        public void testGetAccounts() throws WalletException {
                Account a = wallet.createAccount("Alice");
                Account b = wallet.createAccount("Bob");
                assertTrue(wallet.getAccounts().length == 2);
                for (Account x : wallet.getAccounts())
                        System.out.println(x);
        }

        @Test
        public void testGenerateAddress() throws WalletException {
                GenerateAddressOptions options = new GenerateAddressOptions()
                                .withLedgerNanoPrompt(false);

                String address = wallet.generateEd25519Address(0, 0, options, "rms");
                assertEquals("rms1qpx0mcrqq7t6up73n4na0zgsuuy4p0767ut0qq67ngctj7pg4tm2ynsuynp", address);

                // generated alice account later has 2 addresses premade, so we check against
                // the third
                address = wallet.generateEd25519Address(0, 2, options, "rms");
                assertEquals("rms1qzjq2jwzp8ddh0gawgdskvtd6awlv82c8y0a9s6g7kgszn6ts95u6r4kx2n", address);

                GenerateAddressOptions internal_options = new GenerateAddressOptions()
                                .withLedgerNanoPrompt(false)
                                .withInternal(true);

                String addressPublic = wallet.generateEd25519Address(0, 0, internal_options, "rms");
                assertEquals("rms1qqtjgttzh2dp5exzru94pddle5sqf0007q4smdsaycerff2hny5764xrkgk", addressPublic);

                String anotherAddress = wallet.generateEd25519Address(10, 10, internal_options, "rms");
                assertEquals("rms1qzu4a5ryj39h07z9atn2fza59wu2n5f295st5ehmjg5u8tyveaew65lw3yg", anotherAddress);

                Account account = wallet.createAccount("Alice");
                AccountAddress[] addresses = account
                                .generateEd25519Addresses(new GenerateEd25519Addresses().withAmount(1)
                                                .withGenerateAddressOptions(options));

                assertEquals(1, addresses.length);
                String addrHex = wallet.bech32ToHex(addresses[0].getAddress());
                assertEquals("rms1qz8jdgvrerzv35s43pkdkawdr9x4t6xfnhcrt5tlgsyltgpwyx9ks4c5kct",
                                wallet.hexToBech32(addrHex, "rms"));

                account = wallet.getAccounts()[0];
                addresses = account
                                .generateEd25519Addresses(new GenerateEd25519Addresses().withAmount(1)
                                                .withGenerateAddressOptions(options));
                addrHex = wallet.bech32ToHex(addresses[0].getAddress());
                assertEquals(address, wallet.hexToBech32(addrHex, "rms"));
        }
}
