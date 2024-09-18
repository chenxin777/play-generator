import FileUploader from '@/components/FileUploader';
import { makeGeneratorUsingPost } from '@/services/backend/generatorController';
import { ProFormInstance, ProFormItem } from '@ant-design/pro-components';
import { ProForm } from '@ant-design/pro-form';
import { Collapse, message } from 'antd';
import { saveAs } from 'file-saver';
import { useRef } from 'react';

interface Props {
  meta: any;
}

export default (props: Props) => {
  const { meta } = props;
  const formRef = useRef<ProFormInstance>();

  /**
   * 提交
   * @param values
   */
  const doSubmit = async (values: API.GeneratorMakeRequest) => {
    // 数据转换
    if (!meta.name) {
      message.error('请填写名称');
      return;
    }
    // 文件列表转换为url
    const zipFilePath = values.zipFilePath;
    if (!zipFilePath || zipFilePath.length < 1) {
      message.error('请上传模版文件压缩包');
    }
    //@ts-ignore
    values.zipFilePath = zipFilePath[0].response;

    try {
      // eslint-disable-next-line react-hooks/rules-of-hooks
      const blob = await makeGeneratorUsingPost(
        { meta, zipFilePath: values.zipFilePath },
        {
          responseType: 'blob',
        },
      );
      // 使用file-saver下载文件
      saveAs(blob, meta.name + '.zip');
    } catch (error: any) {
      message.error('制作失败, ' + error.message);
    }
  };

  const formView = (
    <ProForm
      formRef={formRef}
      submitter={{
        searchConfig: {
          submitText: '制作',
        },
        resetButtonProps: {
          hidden: true,
        },
      }}
      onFinish={doSubmit}
    >
      <ProFormItem label="模版文件" name="zipFilePath">
        <FileUploader
          biz="generator_make_template"
          description="请上传压缩包，打包时不要添加最外层目录"
        />
      </ProFormItem>
    </ProForm>
  );
  return (
    <Collapse
      style={{ marginBottom: 24 }}
      items={[{ key: 'maker', label: '生成器制作工具', children: formView }]}
    >
      {formView}
    </Collapse>
  );
};
