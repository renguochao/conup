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

package org.apache.tuscany.sca.binding.corba.testing.exceptions;

/**
* org/apache/tuscany/sca/binding/corba/testing/exceptions/_CalcImplBase.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from exceptions.idl
* sobota, 7 czerwiec 2008 16:12:31 CEST
*/

public abstract class _CalcImplBase extends org.omg.CORBA.portable.ObjectImpl implements
    org.apache.tuscany.sca.binding.corba.testing.exceptions.Calc, org.omg.CORBA.portable.InvokeHandler {

    // Constructors
    public _CalcImplBase() {
    }

    private static java.util.Hashtable _methods = new java.util.Hashtable();
    static {
        _methods.put("div", new java.lang.Integer(0));
        _methods.put("divForSmallArgs", new java.lang.Integer(1));
    }

    public org.omg.CORBA.portable.OutputStream _invoke(String $method,
                                                       org.omg.CORBA.portable.InputStream in,
                                                       org.omg.CORBA.portable.ResponseHandler $rh) {
        org.omg.CORBA.portable.OutputStream out = null;
        java.lang.Integer __method = (java.lang.Integer)_methods.get($method);
        if (__method == null)
            throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

        switch (__method.intValue()) {
            case 0: // org/apache/tuscany/sca/binding/corba/testing/exceptions/Calc/div
            {
                try {
                    double arg1 = in.read_double();
                    double arg2 = in.read_double();
                    double $result = (double)0;
                    $result = this.div(arg1, arg2);
                    out = $rh.createReply();
                    out.write_double($result);
                } catch (org.apache.tuscany.sca.binding.corba.testing.exceptions.CalcPackage.DivByZero $ex) {
                    out = $rh.createExceptionReply();
                    org.apache.tuscany.sca.binding.corba.testing.exceptions.CalcPackage.DivByZeroHelper.write(out, $ex);
                }
                break;
            }

            case 1: // org/apache/tuscany/sca/binding/corba/testing/exceptions/Calc/divForSmallArgs
            {
                try {
                    double arg1 = in.read_double();
                    double arg2 = in.read_double();
                    double $result = (double)0;
                    $result = this.divForSmallArgs(arg1, arg2);
                    out = $rh.createReply();
                    out.write_double($result);
                } catch (org.apache.tuscany.sca.binding.corba.testing.exceptions.CalcPackage.DivByZero $ex) {
                    out = $rh.createExceptionReply();
                    org.apache.tuscany.sca.binding.corba.testing.exceptions.CalcPackage.DivByZeroHelper.write(out, $ex);
                } catch (org.apache.tuscany.sca.binding.corba.testing.exceptions.CalcPackage.NotSupported $ex) {
                    out = $rh.createExceptionReply();
                    org.apache.tuscany.sca.binding.corba.testing.exceptions.CalcPackage.NotSupportedHelper.write(out,
                                                                                                                 $ex);
                }
                break;
            }

            default:
                throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
        }

        return out;
    } // _invoke

    // Type-specific CORBA::Object operations
    private static String[] __ids = {"IDL:org/apache/tuscany/sca/binding/corba/testing/exceptions/Calc:1.0"};

    @Override
    public String[] _ids() {
        return (String[])__ids.clone();
    }

} // class _CalcImplBase
