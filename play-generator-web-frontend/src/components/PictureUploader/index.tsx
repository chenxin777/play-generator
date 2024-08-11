import { COS_HOST } from '@/constants';
import { uploadFileUsingPost } from '@/services/backend/fileController';
import { LoadingOutlined, PlusOutlined } from '@ant-design/icons';
import { Card, Flex, message, Upload, UploadProps } from 'antd';
import React, { useState } from 'react';

interface Props {
  biz: string;
  onChange?: (url: string) => void;
  value?: string;
}

/**
 * 文件上传组件
 * @constructor
 */
const PictureUploader: React.FC<Props> = (props: Props) => {
  const { biz, value, onChange } = props;
  const [loading, setLoading] = useState<boolean>(false);

  const uploadProps: UploadProps = {
    name: 'file',
    multiple: false,
    maxCount: 1,
    listType: 'picture-card',
    showUploadList: false,
    disabled: loading,
    customRequest: async (fileObj: any) => {
      setLoading(true);
      try {
        const res = await uploadFileUsingPost(
          {
            biz,
          },
          {},
          fileObj.file,
        );
        // 拼接完整图片路径
        const fullPath = COS_HOST + res.data;
        onChange?.(fullPath ?? '');
        fileObj.onSuccess(res.data);
      } catch (error: any) {
        message.error('上传失败，' + error.message);
        fileObj.onError(error);
      }
      setLoading(false);
    },
  };

  const uploadButton = (
    <button style={{ border: 0, background: 'none' }} type="button">
      {loading ? <LoadingOutlined /> : <PlusOutlined />}
      <div style={{ marginTop: 8 }}>上传</div>
    </button>
  );
  return (
    <Flex gap={16}>
      <Card title="图片上传">
        <Upload {...uploadProps}>
          {value ? <img src={value} alt="picture" style={{ width: '100%' }} /> : uploadButton}
        </Upload>
      </Card>
    </Flex>
  );
};

export default PictureUploader;
