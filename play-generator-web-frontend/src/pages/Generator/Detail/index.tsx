import { COS_HOST } from '@/constants';
import AuthorInfo from '@/pages/Generator/Detail/components/AuthorInfo';
import FileConfig from '@/pages/Generator/Detail/components/FileConfig';
import ModelConfig from '@/pages/Generator/Detail/components/ModelConfig';
import {
  downloadGeneratorByIdUsingGet,
  getGeneratorVoByIdUsingGet,
} from '@/services/backend/generatorController';
import { Link, useParams } from '@@/exports';
import { useModel } from '@@/plugin-model';
import { DownloadOutlined, EditOutlined } from '@ant-design/icons';
import { PageContainer } from '@ant-design/pro-components';
import '@umijs/max';
import { Button, Card, Col, Image, message, Row, Space, Tabs, Tag, Typography } from 'antd';
import { saveAs } from 'file-saver';
import moment from 'moment';
import React, { useEffect, useState } from 'react';

/**
 * 生成器详情页
 * @constructor
 */
const GeneratorDetailPage: React.FC = () => {
  const { id } = useParams();
  const [data, setData] = useState<API.GeneratorVO>({});
  const [loading, setLoading] = useState<boolean>(true);
  const { initialState } = useModel('@@initialState');
  const { currentUser } = initialState ?? {};
  const my = data?.userId === currentUser?.id;

  const loadData = async () => {
    if (!id) {
      return;
    }
    setLoading(true);
    try {
      const res = await getGeneratorVoByIdUsingGet({ id: Number(id) });
      if (res.data) {
        setData(res.data);
      }
    } catch (error: any) {
      message.error('加载数据失败, ' + error.message);
    }
    setLoading(false);
  };

  /**
   * 标签列表
   * @param tags
   */
  const tagListView = (tags?: string[]) => {
    if (!tags) {
      return <></>;
    }
    return (
      <div style={{ marginBottom: 8 }}>
        {tags.map((tag) => (
          <Tag key={tag}>{tag}</Tag>
        ))}
      </div>
    );
  };

  /**
   * 下载按钮
   */
  const downloadButton = data.distPath && currentUser && (
    <Button
      icon={<DownloadOutlined />}
      onClick={async () => {
        const blob = await downloadGeneratorByIdUsingGet(
          { id: Number(id) },
          {
            responseType: 'blob',
          },
        );

        // 使用file-saver下载文件
        const fullPath = COS_HOST + data.distPath;
        saveAs(blob, fullPath.substring(fullPath.lastIndexOf('/') + 1));
      }}
    >
      下载
    </Button>
  );

  /**
   * 编辑按钮
   */
  const editButton = my && (
    <Link to={`/generator/update?id=${data.id}`}>
      <Button icon={<EditOutlined />}>编辑</Button>
    </Link>
  );
  /**
   * 监听id变化，触发查询
   */
  useEffect(() => {
    if (!id) {
      return;
    }
    loadData();
  }, [id]);

  return (
    <PageContainer title={<></>} loading={loading}>
      <Card>
        <Row justify="space-between" gutter={[32, 32]}>
          <Col flex="auto">
            <Space size="large" align="center">
              <Typography.Title level={4}>{data.name}</Typography.Title>
              {tagListView(data.tags)}
            </Space>
            <Typography.Paragraph>{data.description}</Typography.Paragraph>
            <Typography.Paragraph type="secondary">
              创建时间：{moment(data.createTime).format('YYYY-MM-DD hh:mm:ss')}
            </Typography.Paragraph>
            <Typography.Paragraph type="secondary">基础包：{data.basePackage}</Typography.Paragraph>
            <Typography.Paragraph type="secondary">版本号：{data.version}</Typography.Paragraph>
            <Typography.Paragraph type="secondary">作者：{data.author}</Typography.Paragraph>
            <div style={{ marginBottom: 24 }} />
            <Space size="middle">
              <Link to={`/generator/use/${data.id}`}>
                <Button type="primary">立即使用</Button>
              </Link>
              {downloadButton}
              {editButton}
            </Space>
          </Col>
          <Col flex="320px">
            <Image src={data.picture} />
          </Col>
        </Row>
      </Card>
      <Card>
        <Tabs
          size="large"
          defaultActiveKey="fileConfig"
          onChange={() => {}}
          items={[
            {
              key: 'fileConfig',
              label: '文件配置',
              children: <FileConfig data={data} />,
            },
            {
              key: 'modelConfig',
              label: '模型配置',
              children: <ModelConfig data={data} />,
            },
            {
              key: 'userInfo',
              label: '作者信息',
              children: <AuthorInfo data={data} />,
            },
          ]}
        ></Tabs>
      </Card>
    </PageContainer>
  );
};
export default GeneratorDetailPage;
