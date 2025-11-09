import { Box, Grid, Typography } from '@mui/material'
import { Header } from '@/shared/components/layout/Header'
import {
  AuthenticationCard,
  CharacterCreationCard,
  CharacterRosterCard,
} from '../create/components'

export function CharacterCreationPage() {
  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh', overflow: 'hidden' }}>
      <Header />
      <Box
        sx={{
          flex: 1,
          overflow: 'hidden',
          px: { xs: 2, md: 4 },
          py: { xs: 2, md: 3 },
        }}
      >
        <Grid
          container
          spacing={2}
          sx={{
            height: { md: '100%' },
          }}
        >
          <Grid item xs={12} md={4} sx={{ height: { md: '100%' } }}>
            <AuthenticationCard />
          </Grid>
          <Grid item xs={12} md={4} sx={{ height: { md: '100%' } }}>
            <CharacterCreationCard />
          </Grid>
          <Grid item xs={12} md={4} sx={{ height: { md: '100%' } }}>
            <CharacterRosterCard />
          </Grid>
        </Grid>
      </Box>
      <Box
        sx={{
          px: { xs: 2, md: 4 },
          py: 1.5,
          borderTop: '1px solid rgba(255,255,255,0.06)',
          backgroundColor: 'rgba(0,0,0,0.4)',
        }}
      >
        <Typography variant="caption" color="text.secondary">
          Войдите, настройте нового героя и проверяйте roster без переключения экранов.
        </Typography>
      </Box>
    </Box>
  )
}






