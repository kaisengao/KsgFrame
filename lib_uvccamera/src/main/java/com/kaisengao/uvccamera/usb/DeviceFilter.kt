/*
 *  UVCCamera
 *  library and sample to access to UVC web camera on non-rooted Android device
 *
 * Copyright (c) 2014-2017 saki t_saki@serenegiant.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *  All files in the folder are under this Apache License, Version 2.0.
 *  Files in the libjpeg-turbo, libusb, libuvc, rapidjson folder
 *  may have a different license, see the respective files.
 */
package com.kaisengao.uvccamera.usb

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.hardware.usb.UsbDevice
import android.text.TextUtils
import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.util.*

/**
 * @ClassName: DeviceFilter
 * @Author: KaiSenGao
 * @CreateDate: 2021/4/7 11:50
 * @Description: 设备过滤
 */
class DeviceFilter constructor(
    vid: Int,
    pid: Int,
    clasz: Int,
    subclass: Int,
    protocol: Int,
    manufacturer: String?,
    product: String?,
    serialNum: String?,
    // set true if specific device(s) should exclude
    val isExclude: Boolean = false
) {
    // USB Vendor ID (or -1 for unspecified)
    val mVendorId: Int = vid

    // USB Product ID (or -1 for unspecified)
    val mProductId: Int = pid

    // USB device or interface class (or -1 for unspecified)
    val mClass: Int = clasz

    // USB device subclass (or -1 for unspecified)
    val mSubclass: Int = subclass

    // USB device protocol (or -1 for unspecified)
    val mProtocol: Int = protocol

    // USB device manufacturer name string (or null for unspecified)
    val mManufacturerName: String? = manufacturer

    // USB device product name string (or null for unspecified)
    val mProductName: String? = product

    // USB device serial number string (or null for unspecified)
    val mSerialNumber: String? = serialNum

    /**
     * 返回指定的类、子类协议是否与该DeviceFilter匹配
     * mExclude标志要另外检查是不是#isExclude
     * @param clasz
     * @param subclass
     * @param protocol
     * @return
     */
    private fun matches(clasz: Int, subclass: Int, protocol: Int): Boolean {
        return ((mClass == -1 || clasz == mClass)
                && (mSubclass == -1 || subclass == mSubclass) && (mProtocol == -1 || protocol == mProtocol))
    }

    /**
     * 指定したUsbDeviceがこのDeviceFilterにマッチするかどうかを返す
     * mExcludeフラグは別途#isExcludeか自前でチェックすること
     * @param device
     * @return
     */
    fun matches(device: UsbDevice): Boolean {
        if (mVendorId != -1 && device.vendorId != mVendorId) {
            return false
        }
        if (mProductId != -1 && device.productId != mProductId) {
            return false
        }
        // check device class/subclass/protocol
        if (matches(device.deviceClass, device.deviceSubclass, device.deviceProtocol)) {
            return true
        }
        // if device doesn't match, check the interfaces
        val count = device.interfaceCount
        for (i in 0 until count) {
            val intf = device.getInterface(i)
            if (matches(intf.interfaceClass, intf.interfaceSubclass, intf.interfaceProtocol)) {
                return true
            }
        }
        return false
    }

    override fun equals(other: Any?): Boolean {
        // can't compare if we have wildcard strings
        if (mVendorId == -1 || mProductId == -1 || mClass == -1 || mSubclass == -1 || mProtocol == -1) {
            return false
        }
        if (other is DeviceFilter) {
            if (other.mVendorId != mVendorId || other.mProductId != mProductId || other.mClass != mClass || other.mSubclass != mSubclass || other.mProtocol != mProtocol) {
                return false
            }
            if (other.mManufacturerName != null && mManufacturerName == null
                || other.mManufacturerName == null && mManufacturerName != null
                || other.mProductName != null && mProductName == null
                || other.mProductName == null && mProductName != null
                || other.mSerialNumber != null && mSerialNumber == null
                || other.mSerialNumber == null && mSerialNumber != null
            ) {
                return false
            }
            return if (other.mManufacturerName != null && mManufacturerName != null && mManufacturerName != other.mManufacturerName
                || other.mProductName != null && mProductName != null && mProductName != other.mProductName
                || other.mSerialNumber != null && mSerialNumber != null && mSerialNumber != other.mSerialNumber
            ) {
                false
            } else other.isExclude != isExclude
        }
        if (other is UsbDevice) {
            return !(isExclude
                    || other.vendorId != mVendorId
                    || other.productId != mProductId
                    || other.deviceClass != mClass
                    || other.deviceSubclass != mSubclass
                    || other.deviceProtocol != mProtocol)
        }
        return false
    }

    override fun hashCode(): Int {
        return mVendorId shl 16 or mProductId xor (mClass shl 16
                or (mSubclass shl 8) or mProtocol)
    }

    override fun toString(): String {
        return ("DeviceFilter[mVendorId=" + mVendorId + ",mProductId="
                + mProductId + ",mClass=" + mClass + ",mSubclass=" + mSubclass
                + ",mProtocol=" + mProtocol
                + ",mManufacturerName=" + mManufacturerName
                + ",mProductName=" + mProductName
                + ",mSerialNumber=" + mSerialNumber
                + ",isExclude=" + isExclude
                + "]")
    }

    companion object {

        private const val TAG = "DeviceFilter"

        /**
         * 从指定的xml资源生成DeviceFilter列表
         * @param context
         * @param deviceFilterXmlId
         * @return
         */
        fun getDeviceFilters(context: Context, deviceFilterXmlId: Int): List<DeviceFilter> {
            val parser: XmlPullParser = context.resources.getXml(deviceFilterXmlId)
            val deviceFilters: MutableList<DeviceFilter> = ArrayList()
            try {
                var eventType = parser.eventType
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        val deviceFilter = readEntryOne(context, parser)
                        if (deviceFilter != null) {
                            deviceFilters.add(deviceFilter)
                        }
                    }
                    eventType = parser.next()
                }
            } catch (e: XmlPullParserException) {
                Log.d(TAG, "XmlPullParserException", e)
            } catch (e: IOException) {
                Log.d(TAG, "IOException", e)
            }
            return Collections.unmodifiableList(deviceFilters)
        }

        /**
         * read as integer values with default value from xml(w/o exception throws)
         * resource integer id is also resolved into integer
         * @param parser
         * @param nameSpace
         * @param name
         * @param defaultValue
         * @return
         */
        private fun getAttributeInteger(
            context: Context,
            parser: XmlPullParser,
            nameSpace: String?,
            name: String,
            defaultValue: Int
        ): Int {
            var result = defaultValue
            try {
                var v = parser.getAttributeValue(nameSpace, name)
                if (!TextUtils.isEmpty(v) && v!!.startsWith("@")) {
                    val r = v.substring(1)
                    val resId = context.resources.getIdentifier(r, null, context.packageName)
                    if (resId > 0) {
                        result = context.resources.getInteger(resId)
                    }
                } else {
                    var radix = 10
                    if (v != null && v.length > 2 && v[0] == '0' &&
                        (v[1] == 'x' || v[1] == 'X')
                    ) {
                        // allow hex values starting with 0x or 0X
                        radix = 16
                        v = v.substring(2)
                    }
                    result = v!!.toInt(radix)
                }
            } catch (e: NotFoundException) {
                result = defaultValue
            } catch (e: NumberFormatException) {
                result = defaultValue
            } catch (e: NullPointerException) {
                result = defaultValue
            }
            return result
        }

        /**
         * read as boolean values with default value from xml(w/o exception throws)
         * resource boolean id is also resolved into boolean
         * if the value is zero, return false, if the value is non-zero integer, return true
         * @param context
         * @param parser
         * @param namespace
         * @param name
         * @param defaultValue
         * @return
         */
        private fun getAttributeBoolean(
            context: Context,
            parser: XmlPullParser,
            namespace: String?,
            name: String,
            defaultValue: Boolean
        ): Boolean {
            var result = defaultValue
            try {
                var v = parser.getAttributeValue(namespace, name)
                if ("TRUE".equals(v, ignoreCase = true)) {
                    result = true
                } else if ("FALSE".equals(v, ignoreCase = true)) {
                    result = false
                } else if (!TextUtils.isEmpty(v) && v!!.startsWith("@")) {
                    val r = v.substring(1)
                    val resId = context.resources.getIdentifier(r, null, context.packageName)
                    if (resId > 0) {
                        result = context.resources.getBoolean(resId)
                    }
                } else {
                    var radix = 10
                    if (v != null && v.length > 2 && v[0] == '0' &&
                        (v[1] == 'x' || v[1] == 'X')
                    ) {
                        // allow hex values starting with 0x or 0X
                        radix = 16
                        v = v.substring(2)
                    }
                    val `val` = v!!.toInt(radix)
                    result = `val` != 0
                }
            } catch (e: NotFoundException) {
                result = defaultValue
            } catch (e: NumberFormatException) {
                result = defaultValue
            } catch (e: NullPointerException) {
                result = defaultValue
            }
            return result
        }

        /**
         * read as String attribute with default value from xml(w/o exception throws)
         * resource string id is also resolved into string
         * @param parser
         * @param namespace
         * @param name
         * @param defaultValue
         * @return
         */
        private fun getAttributeString(
            context: Context,
            parser: XmlPullParser,
            namespace: String?,
            name: String,
            defaultValue: String?
        ): String? {
            var result = defaultValue
            try {
                result = parser.getAttributeValue(namespace, name)
                if (result == null) result = defaultValue
                if (!TextUtils.isEmpty(result) && result!!.startsWith("@")) {
                    val r = result.substring(1)
                    val resId = context.resources.getIdentifier(r, null, context.packageName)
                    if (resId > 0) result = context.resources.getString(resId)
                }
            } catch (e: NotFoundException) {
                result = defaultValue
            } catch (e: NumberFormatException) {
                result = defaultValue
            } catch (e: NullPointerException) {
                result = defaultValue
            }
            return result
        }

        @Throws(XmlPullParserException::class, IOException::class)
        fun readEntryOne(context: Context, parser: XmlPullParser): DeviceFilter? {
            var vendorId = -1
            var productId = -1
            var deviceClass = -1
            var deviceSubclass = -1
            var deviceProtocol = -1
            var exclude = false
            var manufacturerName: String? = null
            var productName: String? = null
            var serialNumber: String? = null
            var hasValue = false
            var tag: String
            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                tag = parser.name
                if (!TextUtils.isEmpty(tag) && tag.equals("usb-device", ignoreCase = true)) {
                    if (eventType == XmlPullParser.START_TAG) {
                        hasValue = true
                        vendorId = getAttributeInteger(context, parser, null, "vendor-id", -1)
                        if (vendorId == -1) {
                            vendorId = getAttributeInteger(context, parser, null, "vendorId", -1)
                            if (vendorId == -1) vendorId =
                                getAttributeInteger(context, parser, null, "venderId", -1)
                        }
                        productId = getAttributeInteger(context, parser, null, "product-id", -1)
                        if (productId == -1) productId =
                            getAttributeInteger(context, parser, null, "productId", -1)
                        deviceClass = getAttributeInteger(context, parser, null, "class", -1)
                        deviceSubclass = getAttributeInteger(context, parser, null, "subclass", -1)
                        deviceProtocol = getAttributeInteger(context, parser, null, "protocol", -1)
                        manufacturerName =
                            getAttributeString(context, parser, null, "manufacturer-name", null)
                        if (TextUtils.isEmpty(manufacturerName)) manufacturerName =
                            getAttributeString(context, parser, null, "manufacture", null)
                        productName =
                            getAttributeString(context, parser, null, "product-name", null)
                        if (TextUtils.isEmpty(productName)) productName =
                            getAttributeString(context, parser, null, "product", null)
                        serialNumber =
                            getAttributeString(context, parser, null, "serial-number", null)
                        if (TextUtils.isEmpty(serialNumber)) serialNumber =
                            getAttributeString(context, parser, null, "serial", null)
                        exclude = getAttributeBoolean(context, parser, null, "exclude", false)
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (hasValue) {
                            return DeviceFilter(
                                vendorId, productId, deviceClass,
                                deviceSubclass, deviceProtocol, manufacturerName, productName,
                                serialNumber, exclude
                            )
                        }
                    }
                }
                eventType = parser.next()
            }
            return null
        }
    }
}