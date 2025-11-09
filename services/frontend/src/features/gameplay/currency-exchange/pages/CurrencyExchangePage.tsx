import { useMemo, useState } from 'react'
import {
  Alert,
  Box,
  Button,
  Divider,
  Grid,
  Paper,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import SwapHorizIcon from '@mui/icons-material/SwapHoriz'
import {
  useGetExchangeRates,
  useGetRateHistory,
} from '@/api/generated/currency-exchange/exchange-rates/exchange-rates'
import { useGetAvailablePairs } from '@/api/generated/currency-exchange/exchange-rates/exchange-rates'
import { useConvertCurrency } from '@/api/generated/currency-exchange/currency-operations/currency-operations'
import { CurrencyPairCard } from '../components/CurrencyPairCard'
import { GameLayout } from '@/features/game/components/GameLayout'

const pairTypes = ['MAJOR', 'MINOR', 'EXOTIC'] as const
const periods = ['24h', '7d', '30d', '1y'] as const

export const CurrencyExchangePage = () => {
  const [pairType, setPairType] = useState<(typeof pairTypes)[number]>()
  const [selectedPair, setSelectedPair] = useState('NCRD/EURO')
  const [historyPeriod, setHistoryPeriod] = useState<(typeof periods)[number]>('24h')
  const [amount, setAmount] = useState(1000)
  const [convertFrom, setConvertFrom] = useState('NCRD')
  const [convertTo, setConvertTo] = useState('EURO')

  const exchangeRatesQuery = useGetExchangeRates({})
  const availablePairsQuery = useGetAvailablePairs(pairType ? { type: pairType } : undefined)
  const rateHistoryQuery = useGetRateHistory({ pair: selectedPair, period: historyPeriod })
  const convertMutation = useConvertCurrency()

  const pairs = exchangeRatesQuery.data?.pairs ?? []
  const filteredPairs = useMemo(
    () =>
      pairType
        ? pairs.filter(pair => (pair.pair_type ?? '').toUpperCase() === pairType)
        : pairs,
    [pairs, pairType]
  )

  const leftPanel = (
    <Stack spacing={2}>
      <Typography variant="h6" fontSize="1rem" fontWeight={600}>
        Фильтры
      </Typography>
      <TextField
        label="Тип пары"
        size="small"
        select
        value={pairType ?? ''}
        onChange={event => setPairType(event.target.value ? (event.target.value as typeof pairType) : undefined)}
        SelectProps={{ native: true }}
      >
        <option value="">Все</option>
        {pairTypes.map(type => (
          <option key={type} value={type}>
            {type}
          </option>
        ))}
      </TextField>
      <Divider />
      <Typography variant="subtitle2" fontWeight={600}>
        Доступные пары ({availablePairsQuery.data?.pairs?.length ?? 0})
      </Typography>
      <Typography variant="caption" color="text.secondary">
        {availablePairsQuery.data?.pairs?.slice(0, 6).join(', ') ?? '—'}
      </Typography>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="h6" fontSize="1rem" fontWeight={600}>
        Конвертация
      </Typography>
      <TextField
        label="Сумма"
        size="small"
        type="number"
        value={amount}
        onChange={event => setAmount(Number.parseInt(event.target.value, 10) || 0)}
      />
      <Stack direction="row" spacing={1}>
        <TextField
          label="Из"
          size="small"
          value={convertFrom}
          onChange={event => setConvertFrom(event.target.value)}
        />
        <TextField
          label="В"
          size="small"
          value={convertTo}
          onChange={event => setConvertTo(event.target.value)}
        />
      </Stack>
      <Button
        variant="contained"
        size="small"
        onClick={() =>
          convertMutation.mutate({
            data: { from_currency: convertFrom, to_currency: convertTo, amount },
          })
        }
      >
        Конвертировать
      </Button>
      {convertMutation.data && (
        <Alert severity="success">
          {amount} {convertFrom} → {convertMutation.data.data.converted_amount} {convertTo}
        </Alert>
      )}
      {convertMutation.error && (
        <Alert severity="error">Не удалось выполнить конвертацию. Проверьте валюты.</Alert>
      )}
    </Stack>
  )

  return (
    <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
      <Stack spacing={2} height="100%">
        <Box display="flex" alignItems="center" gap={1}>
          <SwapHorizIcon sx={{ fontSize: '1.5rem', color: 'primary.main' }} />
          <Typography variant="h5" fontWeight={600}>
            Валютная биржа
          </Typography>
        </Box>
        {exchangeRatesQuery.isError && (
          <Alert severity="error">Не удалось загрузить курсы валют.</Alert>
        )}
        <Typography variant="caption" color="text.secondary">
          Курсов доступно: {filteredPairs.length}
        </Typography>
        <Grid container spacing={1.5}>
          {filteredPairs.map(pair => (
            <Grid item xs={12} sm={6} md={4} key={pair.pair_name}>
              <CurrencyPairCard pair={pair} />
            </Grid>
          ))}
        </Grid>

        <Divider />
        <Typography variant="subtitle2" fontWeight={600}>
          История курса
        </Typography>
        <Stack direction="row" spacing={1}>
          <TextField
            select
            size="small"
            label="Пара"
            value={selectedPair}
            onChange={event => setSelectedPair(event.target.value)}
            SelectProps={{ native: true }}
          >
            {pairs.map(pair => (
              <option key={pair.pair_name} value={pair.pair_name}>
                {pair.pair_name}
              </option>
            ))}
          </TextField>
          <TextField
            select
            size="small"
            label="Период"
            value={historyPeriod}
            onChange={event => setHistoryPeriod(event.target.value as typeof historyPeriod)}
            SelectProps={{ native: true }}
          >
            {periods.map(period => (
              <option key={period} value={period}>
                {period}
              </option>
            ))}
          </TextField>
        </Stack>
        {rateHistoryQuery.isError && (
          <Alert severity="warning">Не удалось получить историю курса.</Alert>
        )}
        {rateHistoryQuery.data && (
          <Paper variant="outlined" sx={{ p: 1.5 }}>
            <Typography variant="caption" color="text.secondary">
              Точек данных: {rateHistoryQuery.data.data.history?.length ?? 0}
            </Typography>
          </Paper>
        )}
      </Stack>
    </GameLayout>
  )
}

export default CurrencyExchangePage

