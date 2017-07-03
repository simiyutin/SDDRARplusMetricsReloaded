/*
j8583 A Java implementation of the ISO8583 protocol
Copyright (C) 2011 Enrique Zamudio Lopez

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
*/
package com.solab.iso8583.parse;

import java.text.ParseException;

import com.solab.iso8583.CustomField;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.util.HexCodec;

/** This class is used to parse fields of type LLBIN.
 * 
 * @author Enrique Zamudio
 */
public class LlbinParseInfo extends FieldParseInfo {

	
	public LlbinParseInfo() {
		super(IsoType.LLBIN, 0);
	}

	@Override
	public IsoValue<?> parse(byte[] buf, int pos, CustomField<?> custom) throws ParseException {
		if (pos < 0) {
			throw new ParseException(String.format("Invalid LLBIN position %d", pos), pos);
		} else if (pos+2 > buf.length) {
			throw new ParseException("Insufficient LLBIN header", pos);
		}
		length = ((buf[pos] - 48) * 10) + (buf[pos + 1] - 48);
		if (length < 0) {
			throw new ParseException(String.format("Invalid LLBIN length %d pos %d", length, pos), pos);
		}
		if (length+pos+2 > buf.length) {
			throw new ParseException(String.format("Insufficient data for LLBIN field, pos %d (LEN states '%s')", pos, new String(buf, pos, 2)), pos);
		}
		byte[] binval = length == 0 ? new byte[0] : HexCodec.hexDecode(new String(buf, pos + 2, length));
		if (custom == null) {
			return new IsoValue<byte[]>(type, binval, binval.length, null);
		} else {
			@SuppressWarnings({"unchecked", "rawtypes"})
			IsoValue<?> v = new IsoValue(type, custom.decodeField(
				new String(buf, pos + 2, length)), binval.length, custom);
			if (v.getValue() == null) {
				return new IsoValue<byte[]>(type, binval, binval.length, null);
			}
			return v;
		}
	}

	@Override
	public IsoValue<?> parseBinary(byte[] buf, int pos, CustomField<?> custom) throws ParseException {
		if (pos < 0) {
			throw new ParseException(String.format("Invalid bin LLBIN position %d", pos), pos);
		} else if (pos+1 > buf.length) {
			throw new ParseException("Insufficient bin LLBIN header", pos);
		}
		length = (((buf[pos] & 0xf0) >> 4) * 10) + (buf[pos] & 0x0f);
		if (length < 0) {
			throw new ParseException(String.format("Invalid bin LLBIN length %d pos %d", length, pos), pos);
		}
		if (length+pos+1 > buf.length) {
			throw new ParseException(String.format("Insufficient data for bin LLBIN field, pos %d", pos), pos);
		}
		byte[] _v = new byte[length];
		System.arraycopy(buf, pos+1, _v, 0, length);
		if (custom == null) {
			return new IsoValue<byte[]>(type, _v, null);
		} else {
			@SuppressWarnings({"unchecked", "rawtypes"})
			IsoValue<?> v = new IsoValue(type, custom.decodeField(HexCodec.hexEncode(_v)), custom);
			if (v.getValue() == null) {
				return new IsoValue<byte[]>(type, _v, null);
			}
			return v;
		}
	}

}
