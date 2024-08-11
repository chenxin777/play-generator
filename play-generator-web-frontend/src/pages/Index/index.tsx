import { listGeneratorVoByPageUsingPost } from '@/services/backend/generatorController';
import { Link } from '@@/exports';
import { UserOutlined } from '@ant-design/icons';
import { PageContainer, ProFormSelect, ProFormText, QueryFilter } from '@ant-design/pro-components';
import { Avatar, Card, Flex, Image, List, message, Tabs, TabsProps, Tag } from 'antd';
import Search from 'antd/es/input/Search';
import Paragraph from 'antd/es/typography/Paragraph';
import moment from 'moment';
import React, { useEffect, useState } from 'react';

/**
 * 默认分页参数
 */
const DEFAULT_PAGE_PARAMS: PageRequest = {
  current: 1,
  pageSize: 4,
  sortField: 'createTime',
  sortOrder: 'descend',
};

const IndexPage: React.FC = () => {
  const [loading, setLoading] = useState<boolean>(true);
  const [dataList, setDataList] = useState<API.GeneratorVO[]>([]);
  const [total, setTotal] = useState<number>(0);
  // 搜索条件
  const [searchParams, setSearchParams] = useState<API.GeneratorQueryRequest>({
    ...DEFAULT_PAGE_PARAMS,
  });

  /**
   * 标签
   */
  const items: TabsProps['items'] = [
    {
      key: 'newest',
      label: '最新',
    },
    {
      key: 'recommend',
      label: '推荐',
    },
  ];

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
   * 搜索
   */
  const doSearch = async () => {
    setLoading(true);
    try {
      const res = await listGeneratorVoByPageUsingPost(searchParams);
      setDataList(res.data?.records ?? []);
      setTotal(res.data?.total ?? 0);
    } catch (error: any) {
      message.error('获取数据失败: ' + error.message);
    }
    setLoading(false);
  };

  useEffect(() => {
    doSearch();
  }, [searchParams]);

  return (
    <PageContainer title={<></>}>
      <Flex justify="center">
        <Search
          style={{
            width: '40vw',
            minWidth: 320,
          }}
          placeholder="请搜索生成器"
          allowClear
          enterButton="搜索"
          size="large"
          value={searchParams.searchText}
          onChange={(e) => {
            searchParams.searchText = e.target.value;
          }}
          onSearch={(value) => {
            setSearchParams({
              ...DEFAULT_PAGE_PARAMS,
              searchText: value,
            });
          }}
        />
      </Flex>
      <div style={{ marginBottom: 16 }} />
      <Tabs size="large" defaultActiveKey="newest" items={items} onChange={() => {}}></Tabs>
      <QueryFilter
        span={12}
        labelWidth="auto"
        labelAlign="left"
        style={{ padding: '16px 0' }}
        onFinish={async (values) => {
          setSearchParams({
            ...DEFAULT_PAGE_PARAMS,
            searchText: searchParams.searchText,
            ...values,
          });
        }}
      >
        <ProFormSelect label="标签" name="tags" mode="tags" />
        <ProFormText name="name" label="名称" />
        <ProFormText name="description" label="描述" />
      </QueryFilter>
      <div style={{ marginBottom: 16 }} />
      <List<API.GeneratorVO>
        rowKey="id"
        loading={loading}
        grid={{
          gutter: 16,
          xs: 1,
          sm: 2,
          md: 3,
          lg: 3,
          xl: 4,
          xxl: 4,
        }}
        dataSource={dataList}
        pagination={{
          current: searchParams.current,
          pageSize: searchParams.pageSize,
          total: total,
          onChange(current, pageSize) {
            setSearchParams({
              ...searchParams,
              current,
              pageSize,
            });
          },
        }}
        renderItem={(data) => (
          <List.Item>
            <Link to={`/generator/detail/${data.id}`}>
              <Card hoverable cover={<Image alt={data.name} src={data.picture} />}>
                <Card.Meta
                  title={<a>{data.name}</a>}
                  description={
                    <Paragraph
                      ellipsis={{
                        rows: 2,
                      }}
                      style={{ height: 44 }}
                    >
                      {data.description}
                    </Paragraph>
                  }
                />
                {tagListView(data.tags)}
                <Flex justify="space-between" align="center">
                  <Paragraph type="secondary" style={{ fontSize: 12, alignItems: 'center' }}>
                    {moment(data.createTime).fromNow()}
                  </Paragraph>
                  <div>
                    <Avatar src={data.user?.userAvatar ?? <UserOutlined />} />
                  </div>
                </Flex>
              </Card>
            </Link>
          </List.Item>
        )}
      />
    </PageContainer>
  );
};

export default IndexPage;
