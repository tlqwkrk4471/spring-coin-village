import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Home.css';

const Home = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [coins, setCoins] = useState([]);
  const [coinsLoading, setCoinsLoading] = useState(true);
  const [userAssets, setUserAssets] = useState({
    cash: 0,
    totalCoinValue: 0
  });
  const [popularCoins, setPopularCoins] = useState([]);
  const [popularCoinsLoading, setPopularCoinsLoading] = useState(true);
  const [userRankings, setUserRankings] = useState([]);
  const [userRankingsLoading, setUserRankingsLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    fetch('http://localhost:8080/api/auth/signin/session', { 
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json'
      }
    })
      .then(res => res.ok ? res.json() : null)
      .then(data => {
        setUser(data);
        setLoading(false);
        // 로그인된 사용자가 있으면 코인 정보를 가져옴
        if (data && data.name) {
          fetchUserCoins();
          fetchUserAssets();
          fetchUserRankings();
        } else {
          setCoinsLoading(false);
        }
        // 인기 코인 정보는 항상 가져옴
        fetchPopularCoins();
      })
      .catch(() => {
        setLoading(false);
        // 로그인 실패해도 인기 코인 정보는 가져옴
        fetchPopularCoins();
      });
  }, []);

  const fetchUserCoins = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/user/wallet/coin', {
        method: 'GET',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        }
      });
      
      if (response.ok) {
        const data = await response.json();
        setCoins(data);
        // 코인 총 가치 계산 (price * quantity)
        const totalCoinValue = data.reduce((sum, coin) => sum + (coin.quantity * coin.price), 0);
        setUserAssets(prev => ({ ...prev, totalCoinValue }));
      } else {
        setCoins([]);
      }
    } catch (error) {
      console.error('코인 정보를 가져오는데 실패했습니다:', error);
      setCoins([]);
    } finally {
      setCoinsLoading(false);
    }
  };

  const fetchUserAssets = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/user/wallet/cash', {
        method: 'GET',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        }
      });
      
      if (response.ok) {
        const cashAmount = await response.json();
        setUserAssets(prev => ({ ...prev, cash: cashAmount || 0 }));
      } else {
        console.error('현금 잔고 조회 실패:', response.status);
        setUserAssets(prev => ({ ...prev, cash: 0 }));
      }
    } catch (error) {
      console.error('현금 잔고를 가져오는데 실패했습니다:', error);
      setUserAssets(prev => ({ ...prev, cash: 0 }));
    }
  };

  const fetchPopularCoins = async () => {
    try {
      console.log('인기 코인 정보를 가져오는 중...');
      const response = await fetch('http://localhost:8080/api/coin/all', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json'
        }
      });
      
      console.log('응답 상태:', response.status);
      
      if (response.ok) {
        const data = await response.json();
        console.log('받은 코인 데이터:', data);
        setPopularCoins(data);
      } else {
        console.error('인기 코인 조회 실패:', response.status);
        setPopularCoins([]);
      }
    } catch (error) {
      console.error('인기 코인 정보를 가져오는데 실패했습니다:', error);
      setPopularCoins([]);
    } finally {
      console.log('인기 코인 로딩 완료');
      setPopularCoinsLoading(false);
    }
  };

  const fetchUserRankings = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/user/ranking', {
        method: 'GET',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        }
      });
      
      if (response.ok) {
        const data = await response.json();
        // 응답 데이터에 순위 정보 추가
        const rankingsWithRank = data.map((user, index) => ({
          rank: index + 1,
          name: user.name,
          totalAsset: user.totalAsset
        }));
        setUserRankings(rankingsWithRank);
      } else {
        console.error('유저 순위 조회 실패:', response.status);
        setUserRankings([]);
      }
    } catch (error) {
      console.error('유저 순위를 가져오는데 실패했습니다:', error);
      setUserRankings([]);
    } finally {
      setUserRankingsLoading(false);
    }
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat('ko-KR', {
      style: 'currency',
      currency: 'KRW'
    }).format(price);
  };

  const handleTradeClick = () => {
    navigate('/trade');
  };

  const handleLogout = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/auth/signin/logout', {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        }
      });
      
      if (response.ok) {
        const result = await response.json();
        if (result === true) {
          // 로그아웃 성공 시 홈 화면으로 리다이렉트
          window.location.href = '/';
        } else {
          console.error('로그아웃에 실패했습니다.');
        }
      } else {
        console.error('로그아웃 요청에 실패했습니다.');
      }
    } catch (error) {
      console.error('로그아웃 중 오류가 발생했습니다:', error);
    }
  };

  const totalAssets = userAssets.cash + userAssets.totalCoinValue;

  return (
    <div className="cv-container">
      <header className="cv-header">
        <div className="cv-header-left">
          <h1 className="cv-title">Coin Village</h1>
        </div>
        <div className="cv-header-right">
          {loading ? (
            <div className="cv-loading">로딩 중...</div>
          ) : user && user.name ? (
            <div className="cv-user-section">
              <div className="cv-welcome">환영합니다, <span className="cv-username">{user.name}</span>님!</div>
              <button className="cv-logout-btn" onClick={handleLogout}>로그아웃</button>
            </div>
          ) : (
            <div className="cv-auth-buttons">
              <button className="cv-btn cv-login" onClick={() => navigate('/login')}>로그인</button>
              <button className="cv-btn cv-signup" onClick={() => navigate('/signup')}>회원가입</button>
            </div>
          )}
        </div>
      </header>
      
      {/* 로그인된 사용자 대시보드 */}
      {user && user.name ? (
        <main className="cv-main-dashboard">
          <div className="cv-dashboard">
            {/* 전체 자산 현황 */}
            <div className="cv-assets-overview">
              <h2 className="cv-section-title">전체 자산 현황</h2>
              <div className="cv-assets-grid">
                <div className="cv-asset-card">
                  <div className="cv-asset-icon">💰</div>
                  <div className="cv-asset-info">
                    <h3 className="cv-asset-title">보유 현금</h3>
                    <p className="cv-asset-value">{formatPrice(userAssets.cash)}</p>
                  </div>
                </div>
                <div className="cv-asset-card">
                  <div className="cv-asset-icon">🪙</div>
                  <div className="cv-asset-info">
                    <h3 className="cv-asset-title">코인 가치</h3>
                    <p className="cv-asset-value">{formatPrice(userAssets.totalCoinValue)}</p>
                  </div>
                </div>
                <div className="cv-asset-card">
                  <div className="cv-asset-icon">📊</div>
                  <div className="cv-asset-info">
                    <h3 className="cv-asset-title">총 자산</h3>
                    <p className="cv-asset-value">{formatPrice(totalAssets)}</p>
                  </div>
                </div>
              </div>
            </div>

            {/* 액션 버튼과 콘텐츠를 나란히 배치 */}
            <div className="cv-content-row">
              {/* 왼쪽: 액션 버튼과 인기 코인 */}
              <div className="cv-left-panel">
                {/* 액션 버튼 */}
                <div className="cv-actions">
                  <button className="cv-action-btn cv-trade-btn" onClick={handleTradeClick}>
                    <span className="cv-action-text">코인 매매</span>
                  </button>
                </div>

                {/* 인기 코인 순위 */}
                <div className="cv-popular-coins">
                  <h2 className="cv-section-title">인기 코인 순위</h2>
                  {popularCoinsLoading ? (
                    <div className="cv-popular-loading">인기 코인 정보를 불러오는 중...</div>
                  ) : (
                    <div className="cv-popular-list">
                      {popularCoins.map((coin, index) => (
                        <div key={index} className="cv-popular-item">
                          <div className="cv-popular-rank">{index + 1}</div>
                          <div className="cv-popular-info">
                            <div className="cv-popular-name">
                              <span className="cv-popular-symbol">{coin.symbol}</span>
                              <span className="cv-popular-fullname">{coin.name}</span>
                            </div>
                            <div className="cv-popular-price">
                              <span className="cv-popular-value">{formatPrice(coin.currentPrice)}</span>
                            </div>
                          </div>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              </div>

              {/* 중앙: 총 자산 유저 순위 */}
              <div className="cv-rankings-section">
                <h2 className="cv-section-title">총 자산 유저 순위</h2>
                {userRankingsLoading ? (
                  <div className="cv-rankings-loading">순위 정보를 불러오는 중...</div>
                ) : (
                  <div className="cv-rankings-list">
                    {userRankings.map((user, index) => (
                      <div key={index} className="cv-ranking-item">
                        <div className="cv-ranking-rank">{user.rank}</div>
                        <div className="cv-ranking-info">
                          <div className="cv-ranking-name">{user.name}</div>
                          <div className="cv-ranking-assets">{formatPrice(user.totalAsset)}</div>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </div>

              {/* 오른쪽: 코인 보유 현황 */}
              <div className="cv-coins-section">
                <h2 className="cv-section-title">코인 보유 현황</h2>
                {coinsLoading ? (
                  <div className="cv-coins-loading">코인 정보를 불러오는 중...</div>
                ) : coins.length > 0 ? (
                  <div className="cv-coins-list">
                    {coins.map((coin, index) => (
                      <div key={index} className="cv-coin-item">
                        <div className="cv-coin-header">
                          <h3 className="cv-coin-name">{coin.name}</h3>
                          <span className="cv-coin-symbol">{coin.symbol}</span>
                        </div>
                        <div className="cv-coin-details">
                          <div className="cv-coin-info">
                            <span className="cv-coin-label">보유 수량:</span>
                            <span className="cv-coin-value">{coin.quantity.toLocaleString()}</span>
                          </div>
                          <div className="cv-coin-info">
                            <span className="cv-coin-label">현재 가격:</span>
                            <span className="cv-coin-value">{formatPrice(coin.price)}</span>
                          </div>
                          <div className="cv-coin-info cv-total-value">
                            <span className="cv-coin-label">총 보유 가격:</span>
                            <span className="cv-coin-value">{formatPrice(coin.quantity * coin.price)}</span>
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                ) : (
                  <div className="cv-no-coins">
                    <div className="cv-no-coins-icon">💰</div>
                    <p className="cv-no-coins-text">보유한 코인이 없습니다</p>
                    <p className="cv-no-coins-subtext">코인을 보유해보세요!</p>
                  </div>
                )}
              </div>
            </div>
          </div>
        </main>
      ) : (
        <main className="cv-main">
          <div className="cv-guest-dashboard">
            <div className="cv-guest-welcome">
              <h1 className="cv-guest-title">Coin Village에 오신 것을 환영합니다</h1>
              <p className="cv-guest-subtitle">가상 코인 거래의 새로운 경험을 시작해보세요</p>
            </div>
          </div>
        </main>
      )}
    </div>
  );
};

export default Home; 