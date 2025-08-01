import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Login.css';

const Login = () => {
  const [formData, setFormData] = useState({
    username: '',
    password: ''
  });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.username.trim() || !formData.password.trim()) {
      alert('아이디와 비밀번호를 모두 입력해주세요.');
      return;
    }
    setIsSubmitting(true);
    try {
      const response = await fetch('http://localhost:8080/api/auth/signin', {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          loginId: formData.username,
          password: formData.password
        })
      });
      if (response.ok) {
        const result = await response.json();
        if (result === true) {
          alert('로그인이 완료되었습니다!');
          navigate('/');
        } else {
          alert('아이디가 존재하지 않거나 비밀번호가 잘못되었습니다.');
        }
      } else {
        alert('로그인 요청 중 오류가 발생했습니다.');
      }
    } catch (error) {
      alert('로그인 요청 중 오류가 발생했습니다.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleBack = () => {
    navigate('/');
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <h2 className="login-title">로그인</h2>
        <form onSubmit={handleSubmit} className="login-form">
          <div className="form-group">
            <label htmlFor="username" className="form-label">아이디</label>
            <input
              type="text"
              id="username"
              name="username"
              value={formData.username}
              onChange={handleChange}
              className="form-input"
              placeholder="아이디를 입력하세요"
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="password" className="form-label">비밀번호</label>
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              className="form-input"
              placeholder="비밀번호를 입력하세요"
              required
            />
          </div>
          <div className="button-group">
            <button type="submit" className="login-btn" disabled={isSubmitting}>로그인</button>
            <button type="button" onClick={handleBack} className="back-btn">돌아가기</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Login; 