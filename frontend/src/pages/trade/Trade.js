import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Trade.css';

const Trade = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [coins, setCoins] = useState([]);
  const [coinsLoading, setCoinsLoading] = useState(true);
  const [selectedCoin, setSelectedCoin] = useState(null);
  const [orderType, setOrderType] = useState('limit'); // 'limit' or 'market'
  const [orderSide, setOrderSide] = useState('buy'); // 'buy' or 'sell'
  const [price, setPrice] = useState('');
  const [quantity, setQuantity] = useState('');
  const [pendingOrders, setPendingOrders] = useState([]);
  const [pendingOrdersLoading, setPendingOrdersLoading] = useState(true);
  const [filledOrders, setFilledOrders] = useState([]);
  const [filledOrdersLoading, setFilledOrdersLoading] = useState(true);
  const [assets, setAssets] = useState(null);
  const [assetsLoading, setAssetsLoading] = useState(true);
  const [orderbook, setOrderbook] = useState([]);
  const [orderbookLoading, setOrderbookLoading] = useState(false);

  const [activeTab, setActiveTab] = useState('order'); // 'order', 'pending', 'filled', 'orderbook', or 'assets'
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
        if (data && data.name) {
          fetchCoins();
          fetchPendingOrders();
          fetchFilledOrders();
          fetchAssets();
        } else {
          // 로그인되지 않은 경우 홈으로 리다이렉트
          navigate('/');
        }
      })
      .catch(() => {
        setLoading(false);
        navigate('/');
      });
  }, [navigate]);

  const fetchCoins = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/coin/all', {
        method: 'GET',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        }
      });
      
      if (response.ok) {
        const data = await response.json();
        setCoins(data);
      } else {
        console.error('코인 정보 조회 실패:', response.status);
        setCoins([]);
      }
    } catch (error) {
      console.error('코인 정보를 가져오는데 실패했습니다:', error);
      setCoins([]);
    } finally {
      setCoinsLoading(false);
    }
  };

  const fetchPendingOrders = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/order/active', {
        method: 'GET',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        }
      });
      
      if (response.ok) {
        const data = await response.json();
        // id 필드를 각 주문 객체에 저장 (화면에는 표시하지 않음)
        const ordersWithId = data.map(order => ({
          ...order,
          id: order.id // id 필드를 별도로 저장
        }));
        setPendingOrders(ordersWithId);
      } else {
        console.error('대기 중인 주문 조회 실패:', response.status);
        setPendingOrders([]);
      }
    } catch (error) {
      console.error('대기 중인 주문을 가져오는데 실패했습니다:', error);
      setPendingOrders([]);
    } finally {
      setPendingOrdersLoading(false);
    }
  };

  const fetchFilledOrders = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/order/filled', {
        method: 'GET',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        }
      });
      
      if (response.ok) {
        const data = await response.json();
        // id 필드를 각 주문 객체에 저장 (화면에는 표시하지 않음)
        const ordersWithId = data.map(order => ({
          ...order,
          id: order.id // id 필드를 별도로 저장
        }));
        setFilledOrders(ordersWithId);
      } else {
        console.error('체결된 주문 조회 실패:', response.status);
        setFilledOrders([]);
      }
    } catch (error) {
      console.error('체결된 주문을 가져오는데 실패했습니다:', error);
      setFilledOrders([]);
    } finally {
      setFilledOrdersLoading(false);
    }
  };

  const fetchAssets = async () => {
    try {
      // 현금 정보 가져오기
      const cashResponse = await fetch('http://localhost:8080/api/user/wallet/cash', {
        method: 'GET',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        }
      });
      
      // 코인 정보 가져오기
      const coinsResponse = await fetch('http://localhost:8080/api/user/wallet/coin', {
        method: 'GET',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        }
      });
      
      let cash = 0;
      let coins = [];
      
      if (cashResponse.ok) {
        cash = await cashResponse.json();
      } else {
        console.error('현금 정보 조회 실패:', cashResponse.status);
      }
      
      if (coinsResponse.ok) {
        coins = await coinsResponse.json();
      } else {
        console.error('코인 정보 조회 실패:', coinsResponse.status);
      }
      
      // 총 자산 계산 (코인 가격 정보 포함)
      const totalCoinValue = coins.reduce((sum, coin) => sum + (coin.quantity * coin.price), 0);
      const totalValue = cash + totalCoinValue;
      
      setAssets({
        cash: cash || 0,
        coins: coins,
        totalValue: totalValue
      });
    } catch (error) {
      console.error('자산 정보를 가져오는데 실패했습니다:', error);
      setAssets(null);
    } finally {
      setAssetsLoading(false);
    }
  };

  const fetchOrderbook = async (coinSymbol) => {
    if (!coinSymbol) return;
    
    setOrderbookLoading(true);
    try {
      const response = await fetch(`http://localhost:8080/api/order/active/${coinSymbol.toLowerCase()}`, {
        method: 'GET',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        }
      });
      
      if (response.ok) {
        const data = await response.json();
        setOrderbook(data);
      } else if (response.status === 404) {
        console.log('해당 코인에 대한 호가 정보가 없습니다.');
        setOrderbook([]);
      } else {
        console.error('호가 정보 조회 실패:', response.status);
        setOrderbook([]);
      }
    } catch (error) {
      console.error('호가 정보를 가져오는데 실패했습니다:', error);
      setOrderbook([]);
    } finally {
      setOrderbookLoading(false);
    }
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat('ko-KR', {
      style: 'currency',
      currency: 'KRW'
    }).format(price);
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
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

  const handleCoinSelect = (coin) => {
    setSelectedCoin(coin);
    setPrice(coin.currentPrice.toString());
    fetchOrderbook(coin.symbol);
  };

  const handleOrder = async () => {
    if (!selectedCoin) {
      alert('코인을 선택해주세요.');
      return;
    }

    if (orderType === 'limit' && (!price || !quantity)) {
      alert('가격과 수량을 입력해주세요.');
      return;
    }

    if (orderType === 'market' && !quantity) {
      alert('수량을 입력해주세요.');
      return;
    }

    try {
      // orderType과 orderSide를 대문자로 변환
      const orderTypeUpper = orderType === 'limit' ? 'LIMIT' : 'MARKET';
      const orderSideUpper = orderSide === 'buy' ? 'BUY' : 'SELL';

      const orderData = {
        coinSymbol: selectedCoin.symbol,
        orderType: orderTypeUpper,
        orderSide: orderSideUpper,
        quantity: Number(quantity),
        price: orderType === 'limit' ? Number(price) : null
      };

      const response = await fetch('http://localhost:8080/api/order', {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(orderData)
      });

      if (response.ok) {
        const result = await response.json();
        if (result === true) {
          alert('주문이 성공적으로 접수되었습니다.');
          setPrice('');
          setQuantity('');
          setSelectedCoin(null);
          // 주문 후 대기 중인 주문 목록 새로고침
          fetchPendingOrders();
          fetchFilledOrders();
          fetchAssets();
          fetchCoins();
        } else {
          alert('주문이 접수되지 않았습니다.');
        }
      } else {
        alert('주문 접수에 실패했습니다.');
      }
    } catch (error) {
      console.error('주문 중 오류가 발생했습니다:', error);
      alert('주문 중 오류가 발생했습니다.');
    }
  };



  const handleCancelOrder = async (orderId) => {
    if (!window.confirm('정말로 이 주문을 취소하시겠습니까?')) {
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/api/order/${orderId}`, {
        method: 'DELETE',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        }
      });

      if (response.ok) {
        const result = await response.json();
        if (result === true) {
          alert('주문이 성공적으로 취소되었습니다.');
        } else {
          alert('주문 취소에 실패했습니다. 주문이 이미 처리되었거나 취소할 수 없는 상태입니다.');
        }
        // 주문 취소 후 대기 중인 주문 목록 새로고침
        fetchPendingOrders();
        fetchFilledOrders();
        fetchAssets();
      } else {
        alert('주문 취소 요청에 실패ㅈ습니다. 서버 오류가 발생했습니다.');
      }
    } catch (error) {
      console.error('주문 취소 중 오류가 발생했습니다:', error);
      alert('주문 취소 중 오류가 발생했습니다. 네트워크 연결을 확인해주세요.');
    }
  };

  if (loading) {
    return <div className="trade-loading">로딩 중...</div>;
  }

  return (
    <div className="trade-container">
      <header className="trade-header">
        <div className="trade-header-left">
          <h1 className="trade-title">Coin Village</h1>
        </div>
        <div className="trade-header-right">
          {user && user.name ? (
            <div className="trade-user-section">
              <div className="trade-welcome">환영합니다, <span className="trade-username">{user.name}</span>님!</div>
              <button className="trade-home-btn" onClick={() => navigate('/')}>홈으로</button>
              <button className="trade-logout-btn" onClick={handleLogout}>로그아웃</button>
            </div>
          ) : null}
        </div>
      </header>

      <main className="trade-main">
        <div className="trade-content">
          {/* 코인 목록 */}
          <div className="trade-coins-section">
            <h2 className="trade-section-title">코인 목록</h2>
            {coinsLoading ? (
              <div className="trade-coins-loading">코인 정보를 불러오는 중...</div>
            ) : (
              <div className="trade-coins-list">
                {coins.map((coin, index) => (
                  <div 
                    key={index} 
                    className={`trade-coin-item ${selectedCoin?.symbol === coin.symbol ? 'selected' : ''}`}
                    onClick={() => handleCoinSelect(coin)}
                  >
                    <div className="trade-coin-info">
                      <div className="trade-coin-name">
                        <span className="trade-coin-symbol">{coin.symbol}</span>
                        <span className="trade-coin-fullname">{coin.name}</span>
                      </div>
                      <div className="trade-coin-price">
                        <span className="trade-coin-value">{formatPrice(coin.currentPrice)}</span>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>

          {/* 오른쪽 섹션 (주문 + 자산) */}
          <div className="trade-right-section">
            {/* 주문/대기주문 탭 섹션 */}
            <div className="trade-order-section">
            {/* 탭 헤더 */}
            <div className="trade-tab-header">
              <button 
                className={`trade-tab-button ${activeTab === 'order' ? 'active' : ''}`}
                onClick={() => setActiveTab('order')}
              >
                주문
              </button>
              <button 
                className={`trade-tab-button ${activeTab === 'orderbook' ? 'active' : ''}`}
                onClick={() => setActiveTab('orderbook')}
              >
                호가
              </button>
              <button 
                className={`trade-tab-button ${activeTab === 'pending' ? 'active' : ''}`}
                onClick={() => setActiveTab('pending')}
              >
                대기 주문
              </button>
              <button 
                className={`trade-tab-button ${activeTab === 'filled' ? 'active' : ''}`}
                onClick={() => setActiveTab('filled')}
              >
                체결 주문
              </button>
            </div>

            {/* 주문 탭 */}
            {activeTab === 'order' && (
              <div className="trade-tab-content">
                {selectedCoin ? (
                  <div className="trade-order-form">
                    <div className="trade-selected-coin">
                      <h3>{selectedCoin.name} ({selectedCoin.symbol})</h3>
                      <p className="trade-current-price">현재가: {formatPrice(selectedCoin.currentPrice)}</p>
                    </div>

                    {/* 매수/매도 선택 */}
                    <div className="trade-order-tabs">
                      <button 
                        className={`trade-tab ${orderSide === 'buy' ? 'active' : ''}`}
                        onClick={() => setOrderSide('buy')}
                      >
                        매수
                      </button>
                      <button 
                        className={`trade-tab ${orderSide === 'sell' ? 'active' : ''}`}
                        onClick={() => setOrderSide('sell')}
                      >
                        매도
                      </button>
                    </div>

                    {/* 주문 타입 선택 */}
                    <div className="trade-order-type">
                      <label>
                        <input 
                          type="radio" 
                          value="limit" 
                          checked={orderType === 'limit'} 
                          onChange={(e) => setOrderType(e.target.value)}
                        />
                        지정가 주문
                      </label>
                      <label>
                        <input 
                          type="radio" 
                          value="market" 
                          checked={orderType === 'market'} 
                          onChange={(e) => setOrderType(e.target.value)}
                        />
                        시장가 주문
                      </label>
                    </div>

                                    {/* 가격 입력 (지정가 주문일 때만) */}
                {orderType === 'limit' && (
                  <div className="trade-input-group">
                    <label>희망 가격</label>
                    <div className="trade-input-with-buttons">
                      <input
                        type="number"
                        value={price}
                        onChange={(e) => setPrice(e.target.value)}
                        placeholder="가격을 입력하세요"
                        min="0"
                        step="1"
                      />
                      <div className="trade-price-buttons">
                        <div className="trade-price-row">
                          <button type="button" className="trade-price-btn" onClick={() => setPrice(prev => (Number(prev) || 0) + 1000)}>+1000</button>
                          <button type="button" className="trade-price-btn" onClick={() => setPrice(prev => (Number(prev) || 0) + 100)}>+100</button>
                          <button type="button" className="trade-price-btn" onClick={() => setPrice(prev => (Number(prev) || 0) + 10)}>+10</button>
                        </div>
                        <div className="trade-price-row">
                          <button type="button" className="trade-price-btn" onClick={() => setPrice(prev => Math.max(0, (Number(prev) || 0) - 1000))}>-1000</button>
                          <button type="button" className="trade-price-btn" onClick={() => setPrice(prev => Math.max(0, (Number(prev) || 0) - 100))}>-100</button>
                          <button type="button" className="trade-price-btn" onClick={() => setPrice(prev => Math.max(0, (Number(prev) || 0) - 10))}>-10</button>
                        </div>
                      </div>
                    </div>
                  </div>
                )}

                                    {/* 수량 입력 */}
                <div className="trade-input-group">
                  <label>수량</label>
                  <div className="trade-input-with-buttons">
                    <input
                      type="number"
                      value={quantity}
                      onChange={(e) => setQuantity(e.target.value)}
                      placeholder="수량을 입력하세요"
                      min="0"
                      step="1"
                    />
                    <div className="trade-quantity-buttons">
                      <div className="trade-quantity-row">
                        <button type="button" className="trade-quantity-btn" onClick={() => setQuantity(prev => (Number(prev) || 0) + 100)}>+100</button>
                        <button type="button" className="trade-quantity-btn" onClick={() => setQuantity(prev => (Number(prev) || 0) + 10)}>+10</button>
                        <button type="button" className="trade-quantity-btn" onClick={() => setQuantity(prev => (Number(prev) || 0) + 1)}>+1</button>
                      </div>
                      <div className="trade-quantity-row">
                        <button type="button" className="trade-quantity-btn" onClick={() => setQuantity(prev => Math.max(0, (Number(prev) || 0) - 100))}>-100</button>
                        <button type="button" className="trade-quantity-btn" onClick={() => setQuantity(prev => Math.max(0, (Number(prev) || 0) - 10))}>-10</button>
                        <button type="button" className="trade-quantity-btn" onClick={() => setQuantity(prev => Math.max(0, (Number(prev) || 0) - 1))}>-1</button>
                      </div>
                    </div>
                  </div>
                </div>

                    {/* 주문 버튼 */}
                    <button 
                      className={`trade-order-btn ${orderSide === 'buy' ? 'buy' : 'sell'}`}
                      onClick={handleOrder}
                    >
                      {orderSide === 'buy' ? '매수' : '매도'}
                    </button>
                  </div>
                ) : (
                  <div className="trade-no-selection">
                    <p>매수/매도할 코인을 선택해주세요.</p>
                  </div>
                )}
              </div>
            )}

            {/* 대기 주문 탭 */}
            {activeTab === 'pending' && (
              <div className="trade-tab-content">
                {pendingOrdersLoading ? (
                  <div className="trade-pending-loading">대기 주문 정보를 불러오는 중...</div>
                ) : pendingOrders.length > 0 ? (
                  <div className="trade-pending-list">
                    {pendingOrders.map((order, index) => (
                      <div key={index} className="trade-pending-item">
                        <div className="trade-pending-info">
                          <div className="trade-pending-header">
                            <span className={`trade-pending-side ${order.orderSide === 'BUY' ? 'buy' : 'sell'}`}>
                              {order.orderSide === 'BUY' ? '매수' : '매도'}
                            </span>
                            <span className="trade-pending-symbol">{order.coinSymbol}</span>
                            <span className="trade-pending-name">({order.coinName})</span>
                          </div>
                          <div className="trade-pending-details">
                            <div className="trade-pending-detail">
                              <span className="trade-pending-label">주문 타입:</span>
                              <span className="trade-pending-value">{order.orderType === 'LIMIT' ? '지정가' : '시장가'}</span>
                            </div>
                            {order.orderType === 'LIMIT' && (
                              <div className="trade-pending-detail">
                                <span className="trade-pending-label">희망 가격:</span>
                                <span className="trade-pending-value">{formatPrice(order.price)}</span>
                              </div>
                            )}
                            <div className="trade-pending-detail">
                              <span className="trade-pending-label">수량:</span>
                              <span className="trade-pending-value">{order.quantity.toLocaleString()}</span>
                            </div>
                            <div className="trade-pending-detail">
                              <span className="trade-pending-label">주문 상태:</span>
                              <span className="trade-pending-value">
                                {order.orderStatus === 'PENDING' ? '대기중' : 
                                 order.orderStatus === 'PARTIAL_FILLED' ? '부분체결' : 
                                 order.orderStatus === 'FILLED' ? '전체체결' : 
                                 order.orderStatus === 'CANCELLED' ? '취소됨' : order.orderStatus}
                              </span>
                            </div>
                            <div className="trade-pending-detail">
                              <span className="trade-pending-label">주문 시간:</span>
                              <span className="trade-pending-value">{formatDate(order.createAt)}</span>
                            </div>
                          </div>
                        </div>
                        

                        
                        <button 
                          className="trade-cancel-btn"
                          onClick={() => handleCancelOrder(order.id)}
                        >
                          취소
                        </button>
                      </div>
                    ))}
                  </div>
                ) : (
                  <div className="trade-no-pending">
                    <div className="trade-no-pending-icon">📋</div>
                    <p className="trade-no-pending-text">대기 중인 주문이 없습니다</p>
                    <p className="trade-no-pending-subtext">새로운 주문을 생성해보세요!</p>
                  </div>
                )}
              </div>
            )}

            {/* 호가주문 탭 */}
            {activeTab === 'orderbook' && (
              <div className="trade-tab-content">
                {!selectedCoin ? (
                  <div className="trade-no-selection">
                    <p>호가를 확인할 코인을 선택해주세요.</p>
                  </div>
                ) : orderbookLoading ? (
                  <div className="trade-pending-loading">호가 정보를 불러오는 중...</div>
                ) : orderbook.length > 0 ? (
                  <div className="trade-orderbook-container">
                    <div className="trade-orderbook-header">
                      <h3>{selectedCoin.name} ({selectedCoin.symbol}) 호가창</h3>
                    </div>
                    <div className="trade-orderbook-content">
                      <div className="trade-orderbook-list">
                        {orderbook
                          .sort((a, b) => b.price - a.price)
                          .map((item, index) => {
                            const isCurrentPrice = item.price === selectedCoin.price;
                            return (
                              <div key={index} className={`trade-orderbook-item ${isCurrentPrice ? 'current-price' : ''}`}>
                                <div className="trade-orderbook-sell-quantity">
                                  {item.sellQuantity > 0 && (
                                    <div 
                                      className="trade-orderbook-quantity-bar sell" 
                                      style={{ 
                                        '--bar-width': `${Math.min((item.sellQuantity / 1000) * 100, 100)}%`
                                      }}
                                    ></div>
                                  )}
                                  <span className="trade-orderbook-quantity-text sell">
                                    {item.sellQuantity > 0 ? item.sellQuantity.toLocaleString() : "-"}
                                  </span>
                                </div>
                                <div className="trade-orderbook-divider"></div>
                                <div className="trade-orderbook-price">
                                  <div className="price-main">{formatPrice(item.price)}</div>
                                </div>
                                <div className="trade-orderbook-divider"></div>
                                <div className="trade-orderbook-buy-quantity">
                                  {item.buyQuantity > 0 && (
                                    <div 
                                      className="trade-orderbook-quantity-bar buy" 
                                      style={{ 
                                        '--bar-width': `${Math.min((item.buyQuantity / 1000) * 100, 100)}%`
                                      }}
                                    ></div>
                                  )}
                                  <span className="trade-orderbook-quantity-text buy">
                                    {item.buyQuantity > 0 ? item.buyQuantity.toLocaleString() : "-"}
                                  </span>
                                </div>
                              </div>
                            );
                          })}
                      </div>
                    </div>
                  </div>
                ) : (
                  <div className="trade-no-pending">
                    <div className="trade-no-pending-icon">📊</div>
                    <p className="trade-no-pending-text">호가 정보가 없습니다</p>
                    <p className="trade-no-pending-subtext">해당 코인에 대한 주문이 없습니다.</p>
                  </div>
                )}
              </div>
            )}

            {/* 체결된 주문 탭 */}
            {activeTab === 'filled' && (
              <div className="trade-tab-content">
                {filledOrdersLoading ? (
                  <div className="trade-pending-loading">체결된 주문 정보를 불러오는 중...</div>
                ) : filledOrders.length > 0 ? (
                  <div className="trade-pending-list">
                    {filledOrders.map((order, index) => (
                      <div key={index} className="trade-pending-item">
                        <div className="trade-pending-info">
                          <div className="trade-pending-header">
                            <span className={`trade-pending-side ${order.orderSide === 'BUY' ? 'buy' : 'sell'}`}>
                              {order.orderSide === 'BUY' ? '매수' : '매도'}
                            </span>
                            <span className="trade-pending-symbol">{order.coinSymbol}</span>
                            <span className="trade-pending-name">({order.coinName})</span>
                          </div>
                          <div className="trade-pending-details">
                            <div className="trade-pending-detail">
                              <span className="trade-pending-label">주문 타입:</span>
                              <span className="trade-pending-value">{order.orderType === 'LIMIT' ? '지정가' : '시장가'}</span>
                            </div>
                            {order.orderType === 'LIMIT' && (
                              <div className="trade-pending-detail">
                                <span className="trade-pending-label">체결 가격:</span>
                                <span className="trade-pending-value">{formatPrice(order.price)}</span>
                              </div>
                            )}
                            <div className="trade-pending-detail">
                              <span className="trade-pending-label">수량:</span>
                              <span className="trade-pending-value">{order.quantity.toLocaleString()}</span>
                            </div>
                            <div className="trade-pending-detail">
                              <span className="trade-pending-label">주문 상태:</span>
                              <span className="trade-pending-value">
                                {order.orderStatus === 'PENDING' ? '대기중' : 
                                 order.orderStatus === 'PARTIAL_FILLED' ? '부분체결' : 
                                 order.orderStatus === 'FILLED' ? '전체체결' : 
                                 order.orderStatus === 'CANCELLED' ? '취소됨' : order.orderStatus}
                              </span>
                            </div>
                            <div className="trade-pending-detail">
                              <span className="trade-pending-label">체결 시간:</span>
                              <span className="trade-pending-value">{formatDate(order.createAt)}</span>
                            </div>
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                ) : (
                  <div className="trade-no-pending">
                    <div className="trade-no-pending-icon">📋</div>
                    <p className="trade-no-pending-text">체결된 주문이 없습니다</p>
                    <p className="trade-no-pending-subtext">새로운 주문을 생성해보세요!</p>
                  </div>
                )}
              </div>
            )}
          </div>
          </div>

          {/* 자산 섹션 */}
          <div className="trade-assets-section-main">
            <h2 className="trade-section-title">내 자산</h2>
            {assetsLoading ? (
              <div className="trade-pending-loading">자산 정보를 불러오는 중...</div>
            ) : assets ? (
              <div className="trade-assets-container">
                                    {/* 현금 자산 */}
                    <div className="trade-assets-section">
                      <h3 className="trade-assets-title">현금 자산</h3>
                      <div className="trade-cash-info">
                        <div className="trade-cash-amount">
                          <span className="trade-cash-label">보유 현금:</span>
                          <span className="trade-cash-value" style={{ color: '#2563eb' }}>{formatPrice(assets.cash || 0)}</span>
                        </div>
                      </div>
                    </div>

                                    {/* 코인 자산 */}
                    <div className="trade-assets-section">
                      <h3 className="trade-assets-title">코인 자산</h3>
                      {assets.coins && assets.coins.length > 0 ? (
                        <div className="trade-coins-assets">
                          {assets.coins.map((coin, index) => (
                            <div key={index} className="trade-coin-asset-item">
                              <div className="trade-coin-asset-info">
                                <div className="trade-coin-asset-header">
                                  <span className="trade-coin-asset-symbol">{coin.symbol}</span>
                                  <span className="trade-coin-asset-name">({coin.name})</span>
                                </div>
                                <div className="trade-coin-asset-details">
                                  <div className="trade-coin-asset-detail">
                                    <span className="trade-coin-asset-label">보유 수량:</span>
                                    <span className="trade-coin-asset-value">{coin.quantity.toLocaleString()}</span>
                                  </div>
                                  <div className="trade-coin-asset-detail">
                                    <span className="trade-coin-asset-label">평가 금액:</span>
                                    <span className="trade-coin-asset-value">{formatPrice(coin.quantity * coin.price)}</span>
                                  </div>
                                </div>
                              </div>
                            </div>
                          ))}
                        </div>
                      ) : (
                        <div className="trade-no-assets">
                          <p>보유한 코인이 없습니다.</p>
                        </div>
                      )}
                      <div className="trade-cash-info" style={{ borderTop: '2px solid #e5e7eb', paddingTop: '16px', marginTop: '16px' }}>
                        <div className="trade-cash-amount">
                          <span className="trade-cash-label">코인 평가 금액:</span>
                          <span className="trade-cash-value" style={{ color: '#2563eb' }}>{formatPrice(assets.coins ? assets.coins.reduce((sum, coin) => sum + (coin.quantity * coin.price), 0) : 0)}</span>
                        </div>
                      </div>
                    </div>

                    {/* 총 자산 */}
                    <div className="trade-assets-section">
                      <h3 className="trade-assets-title">총 자산</h3>
                      <div className="trade-cash-info">
                        <div className="trade-cash-amount">
                          <span className="trade-cash-label">총 평가 금액:</span>
                          <span className="trade-cash-value" style={{ color: '#2563eb' }}>{formatPrice(assets.totalValue || 0)}</span>
                        </div>
                      </div>
                    </div>
              </div>
            ) : (
              <div className="trade-no-pending">
                <div className="trade-no-pending-icon">💰</div>
                <p className="trade-no-pending-text">자산 정보를 불러올 수 없습니다</p>
                <p className="trade-no-pending-subtext">잠시 후 다시 시도해주세요.</p>
              </div>
            )}
          </div>
        </div>
      </main>
    </div>
  );
};

export default Trade; 