import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Signup.css';

const Signup = () => {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    name: ''
  });
  const [isIdChecked, setIsIdChecked] = useState(false);
  const [isIdDuplicate, setIsIdDuplicate] = useState(false);
  const [isChecking, setIsChecking] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    if (name === 'username') {
      setIsIdChecked(false);
      setIsIdDuplicate(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.name.trim() || !formData.username.trim() || !formData.password.trim()) {
      alert('모든 항목을 입력해주세요.');
      return;
    }
    if (!isIdChecked || isIdDuplicate) {
      alert('아이디 중복체크를 완료해주세요.');
      return;
    }
    setIsSubmitting(true);
    try {
      const response = await fetch('http://localhost:8080/api/auth/signup', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          username: formData.name,
          loginId: formData.username,
          password: formData.password
        })
      });
      if (response.ok) {
        const result = await response.json();
        if (result === true) {
          alert('회원가입이 완료되었습니다!');
          navigate('/');
        } else {
          alert('회원가입에 실패했습니다.');
        }
      } else {
        alert('회원가입 요청 중 오류가 발생했습니다.');
      }
    } catch (error) {
      alert('회원가입 요청 중 오류가 발생했습니다.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleBack = () => {
    navigate('/');
  };

  const handleCheckUsername = async () => {
    if (!formData.username.trim()) {
      alert('아이디를 입력해주세요.');
      return;
    }
    setIsChecking(true);
    try {
      const response = await fetch(`http://localhost:8080/api/auth/signup/check-id?loginId=${encodeURIComponent(formData.username)}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json'
        }
      });
      if (response.ok) {
        const result = await response.json();
        if (result === false) {
          setIsIdDuplicate(false);
          setIsIdChecked(true);
        } else if (result === true) {
          setIsIdDuplicate(true);
          setIsIdChecked(true);
        }
      } else {
        alert('중복 체크 중 오류가 발생했습니다.');
      }
    } catch (error) {
      console.error('중복 체크 에러:', error);
      alert('중복 체크 중 오류가 발생했습니다.');
    } finally {
      setIsChecking(false);
    }
  };

  const getCheckButtonText = () => {
    if (isChecking) return '확인 중...';
    if (isIdChecked && !isIdDuplicate) return '✓ 사용 가능';
    if (isIdChecked && isIdDuplicate) return '중복된 아이디';
    return '중복 체크';
  };

  const getCheckButtonClass = () => {
    if (isChecking) return 'check-btn checking';
    if (isIdChecked && !isIdDuplicate) return 'check-btn success';
    if (isIdChecked && isIdDuplicate) return 'check-btn error';
    return 'check-btn';
  };

  return (
    <div className="signup-container">
      <div className="signup-card">
        <h2 className="signup-title">회원가입</h2>
        <form onSubmit={handleSubmit} className="signup-form">
          <div className="form-group">
            <label htmlFor="name" className="form-label">이름</label>
            <input
              type="text"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              className="form-input"
              placeholder="이름을 입력하세요"
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="username" className="form-label">아이디</label>
            <div className="input-with-btn">
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
              <button 
                type="button" 
                className={getCheckButtonClass()} 
                onClick={handleCheckUsername}
                disabled={isChecking}
              >
                {getCheckButtonText()}
              </button>
            </div>
            {isIdChecked && isIdDuplicate && (
              <div className="error-message">이미 존재하는 아이디입니다.</div>
            )}
            {isIdChecked && !isIdDuplicate && (
              <div className="success-message">사용 가능한 아이디입니다.</div>
            )}
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
            <button type="submit" className="signup-btn" disabled={isSubmitting}>회원가입</button>
            <button type="button" onClick={handleBack} className="back-btn">돌아가기</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Signup; 