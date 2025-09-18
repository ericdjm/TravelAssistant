import './globals.css'
// import { Inter } from 'next/font/google'  // Removed due to sandbox network restrictions
import StyledComponentsRegistry from './registry'

// const inter = Inter({ subsets: ['latin'] })  // Removed due to sandbox network restrictions

export const metadata = {
  title: 'Travel Assistant',
  description: 'AI-powered travel planning assistant',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body>
        <StyledComponentsRegistry>
          {children}
        </StyledComponentsRegistry>
      </body>
    </html>
  )
}