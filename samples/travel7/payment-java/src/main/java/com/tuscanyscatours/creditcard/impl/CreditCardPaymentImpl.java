/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */

package com.tuscanyscatours.creditcard.impl;

import java.util.logging.Logger;

import org.oasisopen.sca.annotation.Service;

import cn.edu.nju.moon.conup.spi.datamodel.ConupTransaction;

import com.tuscanyscatours.creditcard.CreditCardPayment;
import com.tuscanyscatours.payment.creditcard.CreditCardDetailsType;

@Service(CreditCardPayment.class)
public class CreditCardPaymentImpl implements CreditCardPayment {
	private static final Logger LOGGER = Logger.getLogger(CreditCardPaymentImpl.class.getName());
	
	@ConupTransaction
    public String authorize(CreditCardDetailsType creditCard, float amount) {
        if (creditCard != null) {
        	LOGGER.fine("Checking card: name = " + creditCard.getCardOwner().getName()
                + " number = "
                + creditCard.getCreditCardNumber()
                + " for amount "
                + amount);
        } else {
        	LOGGER.fine("Checking card is null");
        }

        return "OK";
    }
}
