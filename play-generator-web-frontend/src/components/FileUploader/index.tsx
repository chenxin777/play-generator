import { uploadFileUsingPost } from '@/services/backend/fileController';
import { InboxOutlined } from '@ant-design/icons';
import { Card, Flex, message, Upload, UploadFile, UploadProps } from 'antd';
import React, { useState } from 'react';

const { Dragger } = Upload;

interface Props {
  biz: string;
  onChange?: (fileList: UploadFile[]) => void;
  value?: UploadFile[];
  description?: string;
}

/**
 * 文件上传组件
 * @constructor
 */
const FileUploader: React.FC<Props> = (props: Props) => {
  const { biz, value, description, onChange } = props;
  const [loading, setLoading] = useState<boolean>(false);

  const uploadProps: UploadProps = {
    name: 'file',
    multiple: false,
    maxCount: 1,
    fileList: value,
    listType: 'text',
    disabled: loading,
    onChange({ fileList }) {
      onChange?.(fileList);
    },
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
        fileObj.onSuccess(res.data);
      } catch (error: any) {
        message.error('上传失败，' + error.message);
        fileObj.onError(error);
      }
      setLoading(false);
    },
  };
  return (
    <Flex gap={16}>
      <Card title="文件上传">
        <Dragger {...uploadProps}>
          <p className="ant-upload-drag-icon">
            <InboxOutlined />
          </p>
          <p className="ant-upload-text">点击或拖拽文件上传</p>
          <p className="ant-upload-hint">{description}</p>
        </Dragger>
      </Card>
    </Flex>
  );
};

export default FileUploader;
