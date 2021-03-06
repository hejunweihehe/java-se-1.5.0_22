/*
 * @(#)file      SnmpV3Message.java
 * @(#)author    Sun Microsystems, Inc.
 * @(#)version   4.12
 * @(#)date      01/01/17
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.sun.jmx.snmp;

// java imports
//
import java.util.Vector;
import java.net.InetAddress;

// import debug stuff
//
import com.sun.jmx.trace.Trace;
import com.sun.jmx.snmp.internal.SnmpMsgProcessingSubSystem;
import com.sun.jmx.snmp.internal.SnmpSecurityModel;
import com.sun.jmx.snmp.internal.SnmpDecryptedPdu;
import com.sun.jmx.snmp.internal.SnmpSecurityCache;

import com.sun.jmx.snmp.SnmpMsg;
import com.sun.jmx.snmp.SnmpPdu;
import com.sun.jmx.snmp.SnmpStatusException;
import com.sun.jmx.snmp.SnmpTooBigException;
import com.sun.jmx.snmp.SnmpScopedPduBulk;
import com.sun.jmx.snmp.BerException;
import com.sun.jmx.snmp.SnmpScopedPduRequest;
import com.sun.jmx.snmp.BerDecoder;
import com.sun.jmx.snmp.SnmpDefinitions;
import com.sun.jmx.snmp.SnmpEngineId;
import com.sun.jmx.snmp.SnmpScopedPduPacket;
import com.sun.jmx.snmp.BerEncoder;
import com.sun.jmx.snmp.SnmpPduRequestType;
import com.sun.jmx.snmp.SnmpPduBulkType;

/**
 * Is a partially decoded representation of an SNMP V3 packet.
 * <P>
 * This class can be used when developing customized manager or agent.
 * <P>
 * The <CODE>SnmpV3Message</CODE> class is directly mapped onto the 
 * message syntax defined in RFC 2572.
 * <BLOCKQUOTE>
 * <PRE>
 * SNMPv3Message ::= SEQUENCE {
 *          msgVersion INTEGER ( 0 .. 2147483647 ),
 *          -- administrative parameters
 *          msgGlobalData HeaderData,
 *          -- security model-specific parameters
 *          -- format defined by Security Model
 *          msgSecurityParameters OCTET STRING,
 *          msgData  ScopedPduData
 *      }
 *     HeaderData ::= SEQUENCE {
 *         msgID      INTEGER (0..2147483647),
 *         msgMaxSize INTEGER (484..2147483647),
 *
 *         msgFlags   OCTET STRING (SIZE(1)),
 *                    --  .... ...1   authFlag
 *                    --  .... ..1.   privFlag
 *                    --  .... .1..   reportableFlag
 *                    --              Please observe:
 *                    --  .... ..00   is OK, means noAuthNoPriv
 *                    --  .... ..01   is OK, means authNoPriv
 *                    --  .... ..10   reserved, must NOT be used.
 *                    --  .... ..11   is OK, means authPriv
 *
 *         msgSecurityModel INTEGER (1..2147483647)
 *     }
 * </BLOCKQUOTE>
 * </PRE>
 * <p><b>This API is a Sun Microsystems internal API  and is subject 
 * to change without notice.</b></p>
 * @since 1.5
 */
public class SnmpV3Message extends SnmpMsg {
  
    /**
     * Message identifier.
     */
    public int msgId = 0;

    /**
     * Message max size the pdu sender can deal with.
     */
    public int msgMaxSize = 0;
    /**
     * Message flags. Reportable flag  and security level.</P>
     *<PRE>
     * --  .... ...1   authFlag
     * --  .... ..1.   privFlag
     * --  .... .1..   reportableFlag
     * --              Please observe:
     * --  .... ..00   is OK, means noAuthNoPriv
     * --  .... ..01   is OK, means authNoPriv
     * --  .... ..10   reserved, must NOT be used.
     * --  .... ..11   is OK, means authPriv
     *</PRE>
     */
    public byte msgFlags = 0;
    /**
     * The security model the security sub system MUST use in order to deal with this pdu (eg: User based Security Model Id = 3).
     */
    public int msgSecurityModel = 0;
    /**
     * The unmarshalled security parameters.
     */
    public byte[] msgSecurityParameters = null;
    /**
     * The context engine Id in which the pdu must be handled (Generaly the local engine Id).
     */
    public byte[] contextEngineId = null;
    /**
     * The context name in which the OID has to be interpreted.
     */
    public byte[] contextName = null;
    /** The encrypted form of the scoped pdu (Only relevant when dealing with privacy).
     */
    public byte[] encryptedPdu = null;

    /** 
     * Constructor.
     * 
     */
    public SnmpV3Message() {
    }
    /**
     * Encodes this message and puts the result in the specified byte array.
     * For internal use only.
     * 
     * @param outputBytes An array to receive the resulting encoding.
     *
     * @exception ArrayIndexOutOfBoundsException If the result does not fit
     *                                           into the specified array.
     */
    public int encodeMessage(byte[] outputBytes) 
	throws SnmpTooBigException {
        int encodingLength = 0;
	if(isTraceOn()) {
	    trace("encodeMessage", "Can't encode directly V3Message!!!!! Need a SecuritySubSystem");
	}
	throw new IllegalArgumentException("Can't encode");
    }

    /**
     * Decodes the specified bytes and initializes this message.
     * For internal use only.
     * 
     * @param inputBytes The bytes to be decoded.
     *
     * @exception SnmpStatusException If the specified bytes are not a valid encoding.
     */
    public void decodeMessage(byte[] inputBytes, int byteCount) 
        throws SnmpStatusException {
	
        try {
            BerDecoder bdec = new BerDecoder(inputBytes);
            bdec.openSequence();
            version = bdec.fetchInteger();
	    bdec.openSequence();
	    msgId = bdec.fetchInteger();
	    msgMaxSize = bdec.fetchInteger();
	    msgFlags = bdec.fetchOctetString()[0];
	    msgSecurityModel =bdec.fetchInteger();
	    bdec.closeSequence();
	    msgSecurityParameters = bdec.fetchOctetString();
	    if( (msgFlags & SnmpDefinitions.privMask) == 0 ) {
		bdec.openSequence();
		contextEngineId = bdec.fetchOctetString();
		contextName = bdec.fetchOctetString();
		data = bdec.fetchAny();
		dataLength = data.length;
		bdec.closeSequence();
	    }
	    else {
		encryptedPdu = bdec.fetchOctetString();
	    }
            bdec.closeSequence() ;
        }
        catch(BerException x) {
	    x.printStackTrace();
            throw new SnmpStatusException("Invalid encoding") ;
        }
	
	if(isTraceOn()) {
	    trace("decodeMessage", "Unmarshalled message : \n" +
		  "version :" + version + "\n" +
		  "msgId :" + msgId +  "\n" +
		  "msgMaxSize :" + msgMaxSize + "\n" +
		  "msgFlags :" + msgFlags + "\n" +
		  "msgSecurityModel :" + msgSecurityModel + "\n" +
		  "contextEngineId :" + (contextEngineId == null ? null : SnmpEngineId.createEngineId(contextEngineId)) + "\n" +
		  "contextName :" + (contextName == null ? null : new String(contextName)) + "\n" +
		  "data :" + data + "\n" +
		  "dat len :" + ((data == null) ? 0 : data.length) + "\n" +
		  "encryptedPdu :" + encryptedPdu + "\n");
	}
    }

    /**
     * Returns the associated request Id.
     * @param data The flat message.
     * @return The request Id.
     */
    public int getRequestId(byte[] data) throws SnmpStatusException {
	BerDecoder bdec = null;
	int msgId = 0;
	try {
            bdec = new BerDecoder(data);
            bdec.openSequence();
            bdec.fetchInteger();
	    bdec.openSequence();
	    msgId = bdec.fetchInteger();
	}catch(BerException x) {
	    throw new SnmpStatusException("Invalid encoding") ;
	}
	try {
	    bdec.closeSequence();
	}
	catch(BerException x) {
	}
	
	return msgId;
    }

    /**
     * Initializes this message with the specified <CODE>pdu</CODE>.
     * <P>
     * This method initializes the data field with an array of 
     * <CODE>maxDataLength</CODE> bytes. It encodes the <CODE>pdu</CODE>. 
     * The resulting encoding is stored in the data field
     * and the length of the encoding is stored in <CODE>dataLength</CODE>.
     * <p>
     * If the encoding length exceeds <CODE>maxDataLength</CODE>, 
     * the method throws an exception.
     * 
     * @param p The PDU to be encoded.
     * @param maxDataLength The maximum length permitted for the data field.
     *
     * @exception SnmpStatusException If the specified <CODE>pdu</CODE> 
     *   is not valid.
     * @exception SnmpTooBigException If the resulting encoding does not fit
     * into <CODE>maxDataLength</CODE> bytes.
     * @exception ArrayIndexOutOfBoundsException If the encoding exceeds 
     *    <CODE>maxDataLength</CODE>.
     */
    public void encodeSnmpPdu(SnmpPdu p, 
			      int maxDataLength) 
        throws SnmpStatusException, SnmpTooBigException {
	
	SnmpScopedPduPacket pdu = (SnmpScopedPduPacket) p;

	if(isTraceOn()) {
	    trace("encodeSnmpPdu", "Pdu to marshall: \n" +
		  "security parameters : " + pdu.securityParameters + "\n" +
		  "type :" + pdu.type + "\n" +
		  "version :" + pdu.version + "\n" +
		  "requestId :" + pdu.requestId +  "\n" +
		  "msgId :" + pdu.msgId +  "\n" +
		  "msgMaxSize :" + pdu.msgMaxSize + "\n" +
		  "msgFlags :" + pdu.msgFlags + "\n" +
		  "msgSecurityModel :" + pdu.msgSecurityModel + "\n" +
		  "contextEngineId :" + pdu.contextEngineId + "\n" +
		  "contextName :" + pdu.contextName + "\n");
	}

        version = pdu.version;
        address = pdu.address;
        port = pdu.port;
	msgId = pdu.msgId;
	msgMaxSize = pdu.msgMaxSize;
	msgFlags = pdu.msgFlags;
	msgSecurityModel = pdu.msgSecurityModel;

	contextEngineId = pdu.contextEngineId;
	contextName = pdu.contextName;

	securityParameters = pdu.securityParameters;

        //
        // Allocate the array to receive the encoding.
        //
        data = new byte[maxDataLength];
    
        //
        // Encode the pdu
        // Reminder: BerEncoder does backward encoding !
        //
    
        try {
            BerEncoder benc = new BerEncoder(data) ;
            benc.openSequence() ;
            encodeVarBindList(benc, pdu.varBindList) ;

            switch(pdu.type) {

            case pduGetRequestPdu :
            case pduGetNextRequestPdu :
            case pduInformRequestPdu :
            case pduGetResponsePdu :
            case pduSetRequestPdu :
            case pduV2TrapPdu :
            case pduReportPdu :
                SnmpPduRequestType reqPdu = (SnmpPduRequestType) pdu;
                benc.putInteger(reqPdu.getErrorIndex());
                benc.putInteger(reqPdu.getErrorStatus());
                benc.putInteger(pdu.requestId);
                break;

            case pduGetBulkRequestPdu :
                SnmpPduBulkType bulkPdu = (SnmpPduBulkType) pdu;
                benc.putInteger(bulkPdu.getMaxRepetitions());
                benc.putInteger(bulkPdu.getNonRepeaters());
                benc.putInteger(pdu.requestId);
                break ;

            default:
                throw new SnmpStatusException("Invalid pdu type " + String.valueOf(pdu.type)) ;
            }
            benc.closeSequence(pdu.type) ;
            dataLength = benc.trim() ;
        }
        catch(ArrayIndexOutOfBoundsException x) {
            throw new SnmpTooBigException() ;
        }
    }
  
  
    /**
     * Gets the PDU encoded in this message.
     * <P>
     * This method decodes the data field and returns the resulting PDU.
     * 
     * @return The resulting PDU.
     * @exception SnmpStatusException If the encoding is not valid.
     */

    public SnmpPdu decodeSnmpPdu() 
	throws SnmpStatusException {
  
	SnmpScopedPduPacket pdu = null;

        BerDecoder bdec = new BerDecoder(data) ;
        try {
            int type = bdec.getTag() ;
            bdec.openSequence(type) ;
            switch(type) {
      
            case pduGetRequestPdu :
            case pduGetNextRequestPdu :
            case pduInformRequestPdu :
            case pduGetResponsePdu :
            case pduSetRequestPdu :
            case pduV2TrapPdu :
            case pduReportPdu :
                SnmpScopedPduRequest reqPdu = new SnmpScopedPduRequest() ;
                reqPdu.requestId = bdec.fetchInteger() ;
                reqPdu.setErrorStatus(bdec.fetchInteger());
                reqPdu.setErrorIndex(bdec.fetchInteger());
                pdu = reqPdu ;
                break ;

            case pduGetBulkRequestPdu :
                SnmpScopedPduBulk bulkPdu = new SnmpScopedPduBulk() ;
                bulkPdu.requestId = bdec.fetchInteger() ;
                bulkPdu.setNonRepeaters(bdec.fetchInteger());
                bulkPdu.setMaxRepetitions(bdec.fetchInteger());
                pdu = bulkPdu ;
                break ;
            default:
                throw new SnmpStatusException(snmpRspWrongEncoding) ;
            }
            pdu.type = type;
            pdu.varBindList = decodeVarBindList(bdec);
            bdec.closeSequence() ;
        } catch(BerException e) {
            if (isDebugOn()) {
                debug("decodeSnmpPdu", e);
            }
            throw new SnmpStatusException(snmpRspWrongEncoding);
        }
    
        //
        // The easy work.
        //
	pdu.address = address;
        pdu.port = port;
	pdu.msgFlags = msgFlags;
        pdu.version = version;
	pdu.msgId = msgId;
	pdu.msgMaxSize = msgMaxSize;
	pdu.msgSecurityModel = msgSecurityModel;
	pdu.contextEngineId = contextEngineId;
	pdu.contextName = contextName;
	
	pdu.securityParameters = securityParameters;

	if(isTraceOn()) {
	    trace("decodeSnmpPdu", "Unmarshalled pdu : \n" +
		  "type :" + pdu.type + "\n" +
		  "version :" + pdu.version + "\n" +
		  "requestId :" + pdu.requestId + "\n" +
		  "msgId :" + pdu.msgId +  "\n" +
		  "msgMaxSize :" + pdu.msgMaxSize + "\n" +
		  "msgFlags :" + pdu.msgFlags + "\n" +
		  "msgSecurityModel :" + pdu.msgSecurityModel + "\n" +
		  "contextEngineId :" + pdu.contextEngineId + "\n" +
		  "contextName :" + pdu.contextName + "\n");
	}	
        return pdu ;
    }
    
    /**
     * Dumps this message in a string.
     *
     * @return The string containing the dump.
     */
    public String printMessage() {
	StringBuffer sb = new StringBuffer();
	sb.append("msgId : " + msgId + "\n");
	sb.append("msgMaxSize : " + msgMaxSize + "\n");
	sb.append("msgFlags : " + msgFlags + "\n");
	sb.append("msgSecurityModel : " + msgSecurityModel + "\n");

	if (contextEngineId == null) {
            sb.append("contextEngineId : null");
        }
        else {
            sb.append("contextEngineId : {\n");
            sb.append(dumpHexBuffer(contextEngineId, 
				    0, 
				    contextEngineId.length));
            sb.append("\n}\n");
        }

	if (contextName == null) {
            sb.append("contextName : null");
        }
        else {
            sb.append("contextName : {\n");
            sb.append(dumpHexBuffer(contextName, 
				    0, 
				    contextName.length));
            sb.append("\n}\n");
        }
	return sb.append(super.printMessage()).toString();	
    }

    // TRACES & DEBUG
    //---------------
    
    boolean isTraceOn() {
        return Trace.isSelected(Trace.LEVEL_TRACE, Trace.INFO_SNMP);
    }

    void trace(String clz, String func, String info) {
        Trace.send(Trace.LEVEL_TRACE, Trace.INFO_SNMP, clz, func, info);
    }

    void trace(String func, String info) {
        trace(dbgTag, func, info);
    }
    
    boolean isDebugOn() {
        return Trace.isSelected(Trace.LEVEL_DEBUG, Trace.INFO_SNMP);
    }

    void debug(String clz, String func, String info) {
        Trace.send(Trace.LEVEL_DEBUG, Trace.INFO_SNMP, clz, func, info);
    }

    void debug(String clz, String func, Throwable exception) {
        Trace.send(Trace.LEVEL_DEBUG, Trace.INFO_SNMP, clz, func, exception);
    }

    void debug(String func, String info) {
        debug(dbgTag, func, info);
    }
    
    void debug(String func, Throwable exception) {
        debug(dbgTag, func, exception);
    }
    
    String dbgTag = "SnmpV3Message";
}
