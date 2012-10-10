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

package org.apache.tuscany.sca.binding.corba.testing.generated;

/**
* org/apache/tuscany/sca/binding/corba/testing/generated/_PrimitivesSetterStub.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from general_tests.idl
* monday, 23 june 2008 14:12:28 CEST
*/

public class _PrimitivesSetterStub extends org.omg.CORBA.portable.ObjectImpl implements
    org.apache.tuscany.sca.binding.corba.testing.generated.PrimitivesSetter {

    public boolean setBoolean(boolean arg) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("setBoolean", true);
            $out.write_boolean(arg);
            $in = _invoke($out);
            boolean $result = $in.read_boolean();
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return setBoolean(arg);
        } finally {
            _releaseReply($in);
        }
    } // setBoolean

    public char setChar(char arg) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("setChar", true);
            $out.write_char(arg);
            $in = _invoke($out);
            char $result = $in.read_char();
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return setChar(arg);
        } finally {
            _releaseReply($in);
        }
    } // setChar

    public char setWchar(char arg) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("setWchar", true);
            $out.write_wchar(arg);
            $in = _invoke($out);
            char $result = $in.read_wchar();
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return setWchar(arg);
        } finally {
            _releaseReply($in);
        }
    } // setWchar

    public byte setOctet(byte arg) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("setOctet", true);
            $out.write_octet(arg);
            $in = _invoke($out);
            byte $result = $in.read_octet();
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return setOctet(arg);
        } finally {
            _releaseReply($in);
        }
    } // setOctet

    public short setShort(short arg) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("setShort", true);
            $out.write_short(arg);
            $in = _invoke($out);
            short $result = $in.read_short();
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return setShort(arg);
        } finally {
            _releaseReply($in);
        }
    } // setShort

    public short setUnsignedShort(short arg) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("setUnsignedShort", true);
            $out.write_ushort(arg);
            $in = _invoke($out);
            short $result = $in.read_ushort();
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return setUnsignedShort(arg);
        } finally {
            _releaseReply($in);
        }
    } // setUnsignedShort

    public int setLong(int arg) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("setLong", true);
            $out.write_long(arg);
            $in = _invoke($out);
            int $result = $in.read_long();
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return setLong(arg);
        } finally {
            _releaseReply($in);
        }
    } // setLong

    public int setUnsignedLong(int arg) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("setUnsignedLong", true);
            $out.write_ulong(arg);
            $in = _invoke($out);
            int $result = $in.read_ulong();
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return setUnsignedLong(arg);
        } finally {
            _releaseReply($in);
        }
    } // setUnsignedLong

    public long setLongLong(long arg) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("setLongLong", true);
            $out.write_longlong(arg);
            $in = _invoke($out);
            long $result = $in.read_longlong();
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return setLongLong(arg);
        } finally {
            _releaseReply($in);
        }
    } // setLongLong

    public long setUnsignedLongLong(long arg) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("setUnsignedLongLong", true);
            $out.write_ulonglong(arg);
            $in = _invoke($out);
            long $result = $in.read_ulonglong();
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return setUnsignedLongLong(arg);
        } finally {
            _releaseReply($in);
        }
    } // setUnsignedLongLong

    public float setFloat(float arg) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("setFloat", true);
            $out.write_float(arg);
            $in = _invoke($out);
            float $result = $in.read_float();
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return setFloat(arg);
        } finally {
            _releaseReply($in);
        }
    } // setFloat

    public double setDouble(double arg) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("setDouble", true);
            $out.write_double(arg);
            $in = _invoke($out);
            double $result = $in.read_double();
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return setDouble(arg);
        } finally {
            _releaseReply($in);
        }
    } // setDouble

    public String setString(String arg) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("setString", true);
            $out.write_string(arg);
            $in = _invoke($out);
            String $result = $in.read_string();
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return setString(arg);
        } finally {
            _releaseReply($in);
        }
    } // setString

    public String setWstring(String arg) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("setWstring", true);
            $out.write_wstring(arg);
            $in = _invoke($out);
            String $result = $in.read_wstring();
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return setWstring(arg);
        } finally {
            _releaseReply($in);
        }
    } // setWstring

    public org.apache.tuscany.sca.binding.corba.testing.generated.RemoteObject setRemoteObject(org.apache.tuscany.sca.binding.corba.testing.generated.RemoteObject obj) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("setRemoteObject", true);
            org.apache.tuscany.sca.binding.corba.testing.generated.RemoteObjectHelper.write($out, obj);
            $in = _invoke($out);
            org.apache.tuscany.sca.binding.corba.testing.generated.RemoteObject $result =
                org.apache.tuscany.sca.binding.corba.testing.generated.RemoteObjectHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return setRemoteObject(obj);
        } finally {
            _releaseReply($in);
        }
    } // setRemoteObject

    // Type-specific CORBA::Object operations
    private static String[] __ids = {"IDL:org/apache/tuscany/sca/binding/corba/testing/generated/PrimitivesSetter:1.0"};

    @Override
    public String[] _ids() {
        return (String[])__ids.clone();
    }

    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException {
        String str = s.readUTF();
        String[] args = null;
        java.util.Properties props = null;
        org.omg.CORBA.Object obj = org.omg.CORBA.ORB.init(args, props).string_to_object(str);
        org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate();
        _set_delegate(delegate);
    }

    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
        String[] args = null;
        java.util.Properties props = null;
        String str = org.omg.CORBA.ORB.init(args, props).object_to_string(this);
        s.writeUTF(str);
    }
} // class _PrimitivesSetterStub
