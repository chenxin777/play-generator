import FileUploader from '@/components/FileUploader';
import PictureUploader from '@/components/PictureUploader';
import { COS_HOST } from '@/constants';
import {
  addGeneratorUsingPost,
  editGeneratorUsingPost,
  getGeneratorVoByIdUsingGet,
} from '@/services/backend/generatorController';
import { useSearchParams } from '@@/exports';
import {
  PageContainer,
  ProCard,
  ProFormInstance,
  ProFormItem,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  StepsForm,
} from '@ant-design/pro-components';
import '@umijs/max';
import { history } from '@umijs/max';
import { message, UploadFile } from 'antd';
import React, { useEffect, useRef, useState } from 'react';

/**
 * 创建生成器
 * @constructor
 */
const GeneratorAddPage: React.FC = () => {
  const formRef = useRef<ProFormInstance>();

  const [searchParams] = useSearchParams();
  const id = searchParams.get('id');
  const [oldData, setOldData] = useState<API.GeneratorEditRequest>();

  const loadData = async () => {
    if (!id) {
      return;
    }
    try {
      const res = await getGeneratorVoByIdUsingGet({ id: Number(id) });
      if (res.data) {
        const { distPath } = res.data ?? {};
        if (distPath) {
          //@ts-ignore
          res.data.distPath = [
            {
              uid: id,
              name: '文件' + id,
              status: 'done',
              url: COS_HOST + distPath,
              response: distPath,
            } as UploadFile,
          ];
        }
        setOldData(res.data);
      }
    } catch (error: any) {
      message.error('加载数据失败, ' + error.message);
    }
  };

  /**
   * 监听id变化，触发查询
   */
  useEffect(() => {
    if (!id) {
      return;
    }
    loadData();
  }, [id]);

  /**
   * 新增
   * @param values
   */
  const doAdd = async (values: API.GeneratorAddRequest) => {
    // 调用接口
    try {
      const res = await addGeneratorUsingPost(values);
      if (res.data) {
        message.success('创建成功');
        history.push(`generator/detail/${res.data}`);
      }
    } catch (error: any) {
      message.error('创建失败' + error.message);
    }
  };

  /**
   * 修改
   * @param values
   */
  const doUpdate = async (values: API.GeneratorEditRequest) => {
    // 调用接口
    try {
      const res = await editGeneratorUsingPost(values);
      if (res.data) {
        message.success('更新成功');
        history.push(`generator/detail/${id}`);
      }
    } catch (error: any) {
      message.error('更新失败' + error.message);
    }
  };

  /**
   * 提交
   * @param values
   */
  const doSubmit = async (values: API.GeneratorEditRequest | API.GeneratorAddRequest) => {
    // 数据转换
    if (!values.modelConfig) {
      values.modelConfig = {};
    }
    if (!values.fileConfig) {
      values.fileConfig = {};
    }
    // 文件列表转换为url
    if (values.distPath && values.distPath.length > 0) {
      //@ts-ignore
      values.distPath = values.distPath[0].response;
    }
    if (id) {
      await doUpdate({
        ...values,
        id: Number(id),
      });
    } else {
      await doAdd(values);
    }
  };

  return (
    <PageContainer>
      <ProCard>
        {/*创建或者已加载要更新的数据时，才渲染表单，顺利填充默认值*/}
        {(!id || oldData) && (
          <StepsForm<API.GeneratorAddRequest | API.GeneratorEditRequest>
            formRef={formRef}
            formProps={{ initialValues: oldData }}
            onFinish={doSubmit}
          >
            <StepsForm.StepForm name="base" title="基本信息">
              <ProFormText name="name" label="名称" placeholder="请输入名称" />
              <ProFormTextArea name="description" label="描述" placeholder="请输入描述" />
              <ProFormText name="basePackage" label="基础包" placeholder="请输入基础包" />
              <ProFormText name="version" label="版本" placeholder="请输入版本号" />
              <ProFormText name="author" label="作者" placeholder="请输入作者" />
              <ProFormSelect label="标签" mode="tags" name="tags" placeholder="请输入标签列表" />
              <ProFormItem label="图片" name="picture">
                <PictureUploader biz="generator_picture" />
              </ProFormItem>
            </StepsForm.StepForm>
            <StepsForm.StepForm
              name="fileConfig"
              title="文件配置"
              onFinish={async () => {
                return true;
              }}
            ></StepsForm.StepForm>
            <StepsForm.StepForm
              name="modelConfig"
              title="模型配置"
              onFinish={async () => {
                return true;
              }}
            ></StepsForm.StepForm>
            <StepsForm.StepForm name="dist" title="生成器文件">
              <ProFormItem label="产物包" name="distPath">
                <FileUploader biz="generator_dist" description="请上传生成器文件压缩包" />
              </ProFormItem>
            </StepsForm.StepForm>
          </StepsForm>
        )}
      </ProCard>
    </PageContainer>
  );
};
export default GeneratorAddPage;
