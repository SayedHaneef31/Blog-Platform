import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Card,
  CardBody,
  CardHeader,
  Button,
} from '@nextui-org/react';
import { ArrowLeft } from 'lucide-react';
import { apiService, Post, Category, Tag, PostStatus } from '../services/apiService';
import PostForm from '../components/PostForm';
import { useAuth } from '../components/AuthContext';

const EditPostPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { isAuthenticated, token } = useAuth();
  const [post, setPost] = useState<Post | null>(null);
  const [categories, setCategories] = useState<Category[]>([]);
  const [tags, setTags] = useState<Tag[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Debug authentication status
  useEffect(() => {
    console.log('EditPostPage - Authentication status:', {
      isAuthenticated,
      hasToken: !!token,
      tokenLength: token?.length
    });
  }, [isAuthenticated, token]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const [categoriesResponse, tagsResponse] = await Promise.all([
          apiService.getCategories(),
          apiService.getTags()
        ]);

        setCategories(categoriesResponse);
        setTags(tagsResponse);

        if (id) {
          const postResponse = await apiService.getPost(id);
          setPost(postResponse);
        }

        setError(null);
      } catch (err) {
        setError('Failed to load necessary data. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [id]);

  const handleSubmit = async (postData: {
    title: string;
    content: string;
    categoryId: string;
    tagIds: string[];
    status: PostStatus;
  }) => {
    try {
      setIsSubmitting(true);
      setError(null);

      if (id) {
        await apiService.updatePost(id, {
          ...postData,
          id
        });
      } else {
        await apiService.createPost(postData);
      }

      navigate('/');
    } catch (err: any) {
      console.error('Error saving post:', err);
      
      // Handle different types of errors
      if (err.status === 401) {
        setError('Authentication failed. Please log in again.');
        // Don't automatically redirect, let user see the error
      } else if (err.status === 403) {
        setError('You do not have permission to perform this action.');
      } else if (err.status === 400) {
        setError(err.message || 'Invalid data provided. Please check your input.');
      } else {
        setError('Failed to save the post. Please try again.');
      }
      setIsSubmitting(false);
    }
  };

  const handleCancel = () => {
    if (id) {
      navigate(`/posts/${id}`);
    } else {
      navigate('/');
    }
  };

  // Debug function to test authentication
  const testAuthentication = async () => {
    try {
      console.log('Testing authentication...');
      const userProfile = await apiService.getUserProfile();
      console.log('Authentication test successful:', userProfile);
      setError('Authentication test successful!');
    } catch (err: any) {
      console.error('Authentication test failed:', err);
      setError(`Authentication test failed: ${err.message || 'Unknown error'}`);
    }
  };

  if (loading) {
    return (
      <div className="max-w-4xl mx-auto px-4">
        <Card className="w-full animate-pulse">
          <CardBody>
            <div className="h-8 bg-default-200 rounded w-3/4 mb-4"></div>
            <div className="space-y-3">
              <div className="h-4 bg-default-200 rounded w-full"></div>
              <div className="h-4 bg-default-200 rounded w-full"></div>
              <div className="h-4 bg-default-200 rounded w-2/3"></div>
            </div>
          </CardBody>
        </Card>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto px-4">
      <Card className="w-full">
        <CardHeader className="flex justify-between items-center">
          <div className="flex items-center gap-4">
            <Button
              variant="flat"
              startContent={<ArrowLeft size={16} />}
              onClick={handleCancel}
            >
              Back
            </Button>
            <h1 className="text-2xl font-bold">
              {id ? 'Edit Post' : 'Create New Post'}
            </h1>
          </div>
          <Button
            color="secondary"
            variant="flat"
            size="sm"
            onClick={testAuthentication}
          >
            Test Auth
          </Button>
        </CardHeader>

        <CardBody>
          {error && (
            <div className="mb-4 p-4 text-red-500 bg-red-50 rounded-lg">
              {error}
            </div>
          )}

          <PostForm
            initialPost={post}
            onSubmit={handleSubmit}
            onCancel={handleCancel}
            categories={categories}
            availableTags={tags}
            isSubmitting={isSubmitting}
          />
        </CardBody>
      </Card>
    </div>
  );
};

export default EditPostPage;