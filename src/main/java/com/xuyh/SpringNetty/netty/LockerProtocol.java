package com.xuyh.SpringNetty.netty;

public class LockerProtocol {
    // 帧头
    public static final byte HEAD_HIGH = 0x6B;
    public static final byte HEAD_LOW = 0x66;
    // 帧尾
    public static final byte TAIL_HIGH = 0x7B;
    public static final byte TAIL_LOW = 0x70;
    // 端口
    public static final byte PORT = 0x1E;

    public static final byte SPECIAL_0X6B_UNPACK = 0x6B;
    public static final byte SPECIAL_0X99_UNPACK = (byte) 0x99;
    public static final byte SPECIAL_0X7B_UNPACK = 0x7B;

    public static final byte SPECIAL_FOR_PACK = (byte) 0x99;

    public static final byte SPECIAL_0X6B_PACK = (byte) 0x94;
    public static final byte SPECIAL_0X99_PACK = 0x66;
    public static final byte SPECIAL_0X7B_PACK = (byte) 0x84;

    public static final int LEN_MATCH = 0;
    public static final int LEN_SHORT = 1;
    public static final int LEN_NOT_MATCH = 2;

    private byte[] bytesCache= new byte[1024];

    /**
     * 帧头校验函数
     * @param data 数据
     * @param len 待校验的数据长度
     * @param offset 当前偏移量
     * @return 校验完后偏移量:-1，校验失败；0，帧头高位校验成功，已偏移至末尾；n，校验成功
     */
    public static int checkHead (byte[] data, int len, int offset) {
        int result = -1;
        if(data == null || data.length < len + offset) {
           // LogUtilNew.getInstance().d("checkHead:data == null || data.length < len + offset");
        } else {
            for (int i = offset; i < offset + len; i++) {
                if (data[i] == HEAD_HIGH) {
                    if (i == len - 1) {
                        result = 0;
                    } else if (data[i + 1] == HEAD_LOW) {
                        result = i + 1;
                        break;
                    }
                }
            }
        }

        return result;
    }


    /**
     * 帧尾校验函数
     * @param data 数据
     * @param len 待校验的数据长度
     * @param offset 当前偏移量
     * @return 校验完后偏移量:-1，校验失败；0，帧尾高位校验成功，已偏移至末尾；n，校验成功
     */
    public static int checkTail (byte[] data, int len, int offset) {
        int result = -1;
        if(data == null || data.length < len + offset) {
           // LogUtilNew.getInstance().d("checkTail:data == null || data.length < len + offset");
        } else {
            for (int i = offset; i < offset + len; i++) {
                if (data[i] == TAIL_HIGH) {
                    if (i == len - 1) {
                        result = 0;
                    } else if (data[i + 1] == TAIL_LOW) {
                        result = i + 1;
                        break;
                    }
                }
            }
        }

        return result;
    }


    /**
     * 特征字解包函数(在帧头帧尾校验函数checkHT处理后调用)
     * @param protocol 待解包的数据
     * @return 完成解包的数据
     */
    public static byte[] unpack (byte[] protocol) {
        byte[] result;
        int count = 0;
        for (int i = 2; i < protocol.length - 2; i++) {
            if (protocol[i] == SPECIAL_FOR_PACK) {
                // i= protocol.length-3 时unpack函数有瑕疵(protocol[protocol.length-3]=0x99,protocol[protocol.length-3]=0x6A)
                // 可以在长度校验函数在进行筛选
                if (protocol[i+1] == SPECIAL_0X6B_PACK
                        || protocol[i+1] == SPECIAL_0X99_PACK
                        || protocol[i+1] == SPECIAL_0X7B_PACK) {
                    count++;
                }
            }
        }
        result = new byte[protocol.length - count];
        for (int i = 0, j = 0; i < result.length; i++, j++) {
            if (protocol[j] == SPECIAL_FOR_PACK) {
                if (protocol[j+1] == SPECIAL_0X6B_PACK) {
                    result[i] = SPECIAL_0X6B_UNPACK;
                    j++;
                } else if (protocol[j+1] == SPECIAL_0X99_PACK) {
                    result[i] = SPECIAL_0X99_UNPACK;
                    j++;
                } else if (protocol[j+1] == SPECIAL_0X7B_PACK) {
                    result[i] = SPECIAL_0X7B_UNPACK;
                    j++;
                } else {
                   // LogUtilNew.getInstance().d("unpack:出现不符合协议的数据");
                    result[i] = protocol[j];
                }

            } else {
                result[i] = protocol[j];
            }
        }

        return result;
    }



    /**
     * 长度校验函数(在特征字解包函数unpack处理后调用)
     * @param protocol 已验证帧头帧尾的单条数据帧
     * @return 校验位
     */
    public static int checkLen (byte[] protocol) {
        int result = LEN_MATCH;
        int len = (protocol[2] < 0 ? 256 + protocol[2] : protocol[2]) * 256
                + protocol[3] < 0 ? 256 + protocol[3] : protocol[3];
        if (protocol.length < 10) {
            result = LEN_SHORT;
           // LogUtilNew.getInstance().d("checkLen:LEN_SHORT");
        }
        if (protocol.length - 4 != len) {
            result = LEN_NOT_MATCH;
           // LogUtilNew.getInstance().d("checkLen:LEN_NOT_MATCH");
        }
        return result;
    }
    /**
     * 计算校验位
     * @param protocol 已验证帧头帧尾的单条数据帧
     * @return 校验位
     */
    public static byte calcCheckBit (byte[] protocol) {
        byte checkBit = 0x00;
        for (int i = 0; i < protocol.length - 3; i++) {
            checkBit = (byte) (checkBit + protocol[i]);
        }
        return checkBit;
    }

    /**
     * 特征字打包函数
     * @param protocol 待打包的数据
     * @return 完成打包的数据
     */
    public static byte[] pack (byte[] protocol) {

        byte[] result;

        if (protocol != null) {
            int count = 0;
            for (int i = 2; i < protocol.length - 2; i++) {
                if (protocol[i] == SPECIAL_0X6B_UNPACK
                        || protocol[i] == SPECIAL_0X99_UNPACK
                        || protocol[i] == SPECIAL_0X7B_UNPACK) {
                    count++;
                }
            }

            if (count == 0) {
                result = protocol;
            } else {
                // 如果存在特征字
                result = new byte[protocol.length + count];
                result[0] = HEAD_HIGH;
                result[1] = HEAD_LOW;
                result[result.length - 2] = TAIL_HIGH;
                result[result.length - 1] = TAIL_LOW;

                for (int i = 2, j = 2; j < result.length - 2; i++, j++) {
                    if (protocol[i] == SPECIAL_0X6B_UNPACK) {
                        result[j++] = SPECIAL_FOR_PACK;
                        result[j] = SPECIAL_0X6B_PACK;
                    } else if (protocol[i] == SPECIAL_0X99_UNPACK) {
                        result[j++] = SPECIAL_FOR_PACK;
                        result[j] = SPECIAL_0X99_PACK;
                    } else if (protocol[i] == SPECIAL_0X7B_UNPACK) {
                        result[j++] = SPECIAL_FOR_PACK;
                        result[j] = SPECIAL_0X7B_PACK;
                    } else {
                        result[j] = protocol[i];
                    }
                }
            }
        } else {
            result = null;
        }
        return result;
    }

    /**
     * 初始化待发送的数据(配置帧头、帧尾、帧长度)
     * @param data 待发送的数据
     * @return 完成配置的数据
     */
    public static void initMessage (byte[] data) {
        // 去除帧头帧尾的长度
        int len = data.length - 4;

        data[0] = HEAD_HIGH;
        data[1] = HEAD_LOW;

        data[2] = (byte) (len / 256);
        data[3] = (byte) (len % 256);
        data[6] = PORT;
        data[data.length - 2] = TAIL_HIGH;
        data[data.length - 1] = TAIL_LOW;
    }



}
