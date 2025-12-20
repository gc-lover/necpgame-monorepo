#!/usr/bin/env python3
"""
JWT Token Generator for NECP Game Services
Генерирует JWT токены для тестирования API endpoints
"""

import jwt
import json
import sys
from datetime import datetime, timedelta
import argparse

def generate_token(secret, user_id="test-user-123", roles=None, expiry_hours=24):
    """
    Генерирует JWT токен с указанными параметрами

    Args:
        secret (str): JWT секретный ключ
        user_id (str): ID пользователя
        roles (list): Список ролей пользователя
        expiry_hours (int): Время жизни токена в часах

    Returns:
        str: JWT токен
    """
    if roles is None:
        roles = ["player"]

    payload = {
        "sub": user_id,
        "iat": datetime.utcnow(),
        "exp": datetime.utcnow() + timedelta(hours=expiry_hours),
        "roles": roles,
        "iss": "necp-game-backend",
        "aud": "necp-game-api"
    }

    token = jwt.encode(payload, secret, algorithm="HS256")
    return token

def main():
    parser = argparse.ArgumentParser(description='Generate JWT token for NECP Game API testing')
    parser.add_argument('--secret', default='your-jwt-secret-change-in-production',
                       help='JWT secret key (default: development secret)')
    parser.add_argument('--user-id', default='test-user-123',
                       help='User ID to include in token')
    parser.add_argument('--roles', nargs='+', default=['player'],
                       help='User roles (default: player)')
    parser.add_argument('--expiry', type=int, default=24,
                       help='Token expiry in hours (default: 24)')
    parser.add_argument('--output', choices=['token', 'curl', 'json'], default='token',
                       help='Output format: token, curl command, or full JSON')

    args = parser.parse_args()

    try:
        token = generate_token(args.secret, args.user_id, args.roles, args.expiry)

        if args.output == 'token':
            print(token)
        elif args.output == 'curl':
            print(f'curl -H "Authorization: Bearer {token}" \\')
            print('     http://localhost:8100/api/v1/achievements')
        elif args.output == 'json':
            payload = jwt.decode(token, args.secret, algorithms=["HS256"])
            output = {
                "token": token,
                "payload": payload,
                "curl_example": f'curl -H "Authorization: Bearer {token}" http://localhost:8100/api/v1/achievements'
            }
            print(json.dumps(output, indent=2, default=str))

    except Exception as e:
        print(f"Error generating token: {e}", file=sys.stderr)
        sys.exit(1)

if __name__ == "__main__":
    main()
