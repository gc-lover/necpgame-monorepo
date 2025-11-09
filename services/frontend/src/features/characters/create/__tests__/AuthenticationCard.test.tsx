import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { fireEvent, render, screen, waitFor } from '@testing-library/react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { AuthenticationCard } from '../components/AuthenticationCard'

const mockUseLogin = vi.fn()
const mockUseRegister = vi.fn()

vi.mock('@/api/generated/auth/auth/auth', () => ({
  useLogin: (...args: unknown[]) => mockUseLogin(...args),
  useRegister: (...args: unknown[]) => mockUseRegister(...args),
}))

describe('AuthenticationCard', () => {
  let queryClient: QueryClient
  const loginMutate = vi.fn()
  const registerMutate = vi.fn()

  beforeEach(() => {
    queryClient = new QueryClient({
      defaultOptions: {
        queries: {
          retry: false,
        },
      },
    })
    mockUseLogin.mockReturnValue({ mutate: loginMutate, isPending: false })
    mockUseRegister.mockReturnValue({ mutate: registerMutate, isPending: false })
    loginMutate.mockReset()
    registerMutate.mockReset()
    vi.spyOn(window.localStorage.__proto__, 'setItem')
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  const renderComponent = () =>
    render(
      <QueryClientProvider client={queryClient}>
        <AuthenticationCard />
      </QueryClientProvider>
    )

  it('отправляет данные входа и сохраняет токен', async () => {
    loginMutate.mockImplementation((_payload, options) => {
      options?.onSuccess?.({ token: 'jwt', account_id: 'acc', expires_at: '' })
    })

    renderComponent()

    fireEvent.change(screen.getByLabelText(/email или логин/i), { target: { value: 'player' } })
    fireEvent.change(screen.getByLabelText(/пароль/i), { target: { value: 'Secret123' } })
    fireEvent.click(screen.getByRole('button', { name: /войти/i }))

    await waitFor(() => {
      expect(loginMutate).toHaveBeenCalled()
      expect(window.localStorage.setItem).toHaveBeenCalledWith('auth_token', 'jwt')
      expect(screen.getByText(/вход выполнен/i)).toBeInTheDocument()
    })
  })

  it('показывает ошибку если поля входа пустые', () => {
    renderComponent()
    fireEvent.click(screen.getByRole('button', { name: /войти/i }))
    expect(screen.getByText(/заполните логин и пароль/i)).toBeInTheDocument()
    expect(loginMutate).not.toHaveBeenCalled()
  })

  it('отправляет данные регистрации при валидной форме', async () => {
    registerMutate.mockImplementation((_payload, options) => {
      options?.onSuccess?.({ account_id: 'new-account' })
    })

    renderComponent()

    fireEvent.click(screen.getByRole('tab', { name: /регистрация/i }))
    fireEvent.change(screen.getByLabelText(/^email$/i), { target: { value: 'player@example.com' } })
    fireEvent.change(screen.getByLabelText(/имя пользователя/i), { target: { value: 'player' } })
    const passwordFields = screen.getAllByLabelText(/пароль/i)
    fireEvent.change(passwordFields[0], { target: { value: 'Secret123' } })
    fireEvent.change(passwordFields[1], { target: { value: 'Secret123' } })
    fireEvent.click(screen.getByRole('checkbox'))
    fireEvent.click(screen.getByRole('button', { name: /зарегистрироваться/i }))

    await waitFor(() => {
      expect(registerMutate).toHaveBeenCalledWith(
        {
          data: {
            email: 'player@example.com',
            username: 'player',
            password: 'Secret123',
            password_confirm: 'Secret123',
            terms_accepted: true,
          },
        },
        expect.any(Object)
      )
      expect(screen.getByText(/аккаунт создан/i)).toBeInTheDocument()
    })
  })

  it('валидация регистрации предотвращает отправку при расхождении паролей', () => {
    renderComponent()

    fireEvent.click(screen.getByRole('tab', { name: /регистрация/i }))
    fireEvent.change(screen.getByLabelText(/^email$/i), { target: { value: 'player@example.com' } })
    fireEvent.change(screen.getByLabelText(/имя пользователя/i), { target: { value: 'player' } })
    const passwordFields = screen.getAllByLabelText(/пароль/i)
    fireEvent.change(passwordFields[0], { target: { value: 'Secret123' } })
    fireEvent.change(passwordFields[1], { target: { value: 'Another' } })
    fireEvent.click(screen.getByRole('checkbox'))
    fireEvent.click(screen.getByRole('button', { name: /зарегистрироваться/i }))

    expect(registerMutate).not.toHaveBeenCalled()
    expect(screen.getByText(/пароли не совпадают/i)).toBeInTheDocument()
  })
})






